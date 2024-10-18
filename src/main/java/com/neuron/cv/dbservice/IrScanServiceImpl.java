package com.neuron.cv.dbservice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.constants.CVLookUpDetails;
import com.neuron.cv.dbentity.CvOrder;
import com.neuron.cv.dbentity.InspectionReport;
import com.neuron.cv.dbentity.Scan;
import com.neuron.cv.dbentity.User;
import com.neuron.cv.dbrepo.CvOrderRepoImpl;
import com.neuron.cv.dbrepo.InspectionReportRepoImpl;
import com.neuron.cv.dbrepo.IrScanRepoImpl;
import com.neuron.cv.dto.InspectionReportResultDto;
import com.neuron.cv.dto.InstaplanDto;
import com.neuron.cv.dto.LevelDto;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.dto.RoomDto;
import com.neuron.cv.dto.SmartTagDto;
import com.neuron.cv.dto.StructureDto;
import com.neuron.cv.dto.UnitDto;
import com.neuron.cv.entity.CvPhoto;
import com.neuron.cv.entity.PropertyCvPhoto;
import com.neuron.cv.entity.Root;
import com.neuron.cv.service.IrStorjService;
import com.neuron.cv.service.PopulateCVJson;
import com.neuron.cv.util.MetricsCalucatorService;
import com.neuron.cv.util.ScanToInstaplanDtoConvertor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IrScanServiceImpl {

	@Value("${storj.bucketName}")
	private String bucketName;

	@Autowired
	public IrScanRepoImpl scanRepoImpl;

	@Autowired
	public ScanToInstaplanDtoConvertor scanToInstaplanDtoConvertor;

	@Autowired
	private InspectionReportRepoImpl inspectionReportRepoImpl;

	@Autowired
	public CvOrderRepoImpl cvOrderRepoImpl;

	@Autowired
	public IrStorjService storjService;

	@Autowired
	public PopulateCVJson populateCVJson;

	@Autowired
	public MetricsCalucatorService metricsCalucatorService;

	@Autowired
	public IrMarkerFileService markerFileService;

	public InspectionReportResultDto generateCVReport(ParamDTO paramDTO) {
		InspectionReportResultDto inspectionReportDto = new InspectionReportResultDto();
		Root root = new Root();
		log.info("generateCVReport: started");

		String addressJsonStr = markerFileService.getAddressFromPath(paramDTO.getFolderPath());
		JSONObject addressJson = new JSONObject(addressJsonStr);
		paramDTO.setAddress(addressJson.getString(CVConstants.JSON_FIELD_ADDRESS));
		paramDTO.setHouseNo(addressJson.getString(CVConstants.JSON_FIELD_UNIT_NO));
		
		List<Scan> scans = scanRepoImpl.getScanDetailsforCVReport(paramDTO);
		String scansfile = this.getScanDetailsforCVReport(paramDTO,addressJson);
//		List<Object[]> spinResults = scanRepoImpl.getSpinCaptureDetailsforCVReport(paramDTO);
		List<InspectionReport> reports = inspectionReportRepoImpl.getInspectionReports(paramDTO);
		String reportsfile = this.getInspectionReports(paramDTO);
		log.info("generateCVReport: got results from database: ");
		InstaplanDto instaplanDto = new InstaplanDto();
		  LocalDateTime firstSpinCapturedAt = LocalDateTime.parse(addressJson.getString(CVConstants.JSON_FIELD_FIRST_SPIN_CAPTURED_AT));
		  Instant instant = firstSpinCapturedAt.atZone(ZoneId.systemDefault()).toInstant();
		  instaplanDto.setFirstSpinCapturedAt((Instant) instant);
		  LocalDateTime lastSpinCapturedAt = LocalDateTime.parse(addressJson.getString(CVConstants.JSON_FIELD_LAST_SPIN_CAPTURED_AT));
		  instant = lastSpinCapturedAt.atZone(ZoneId.systemDefault()).toInstant();
		  instaplanDto.setLastSpinCapturedAt((Instant) instant);
//		if (!spinResults.isEmpty()) {
//			log.info("generateCVReport: setting  FirstSpinCaptured and LastSpinCaptured timestamps: ");
//			for (Object[] spinCaptureDetails : spinResults) {
//				instaplanDto.setFirstSpinCapturedAt((Instant) spinCaptureDetails[0]);
//				instaplanDto.setLastSpinCapturedAt((Instant) spinCaptureDetails[1]);
//			}
//		}

		CVLookUpDetails.setupSmartTagTypes();
		CVLookUpDetails.setupUpdateTagTypes();
		CVLookUpDetails.setupRoomNames();
		String markerFile = "";
		try {
			markerFile = markerFileService.getInspectionReportSessionFile(paramDTO.getFolderPath(),
					paramDTO.getSessionId());
		} catch (Exception e) {
			log.error("generateCVReport: Exception: " + e.getMessage());
		}
		// downloading inspectionreport-sessionId.json file		
		String propCalcs = markerFileService.getPropCalc(paramDTO.getFolderPath(), markerFile);
		JSONObject propCalcsObj = new JSONObject();
		if(propCalcs!=null && !propCalcs.isEmpty())
			propCalcsObj = new JSONObject(propCalcs);
		instaplanDto = metricsCalucatorService.getPropertyCalcProps(propCalcsObj, instaplanDto);
		//imagenames
		markerFileService.processJpgList(markerFile);

		Map<String, String> scanRequiredFields = new HashMap<>();
		User owner = new User();
		if (!CollectionUtils.isEmpty(scans)) {
			owner = scans.get(0).getOwner();
			instaplanDto = scanToInstaplanDtoConvertor.setScanLevelProps(scans.get(0), instaplanDto);
			// instaplanDto = metricsCalucatorService.getPropertyCalcProps(scans.get(0),
			// instaplanDto);
			inspectionReportDto.setModel_url_3d(scans.get(0).getModel_url_3d());
			scanRequiredFields.put("scanTime", scans.get(0).getCreatedAt() + "");
			scanRequiredFields.put("userID", owner.getId() + "");
			scanRequiredFields.put("companyName", owner.getCompanyName());
			scanRequiredFields.put("firstName", owner.getName());
			scanRequiredFields.put("lastName", owner.getName());
			scanRequiredFields.put("phone", owner.getPhoneNumber());
			scanRequiredFields.put("email", owner.getEmail());
		}

		if (!CollectionUtils.isEmpty(reports)) {
			scanRequiredFields.put("reportId", reports.get(0).getReportId() + "");
		}

		for (Scan scan : scans) {
			scanRequiredFields.put(CVConstants.PROPERTY_STRUCTURE, scan.getStructure());
			scanRequiredFields.put(CVConstants.PROPERTY_STRUCTURE_TYPE, scan.getStructureType());
			scanRequiredFields.put(CVConstants.PROPERTY_GRADE_LEVEL, scan.getGradeLevel());
			scanRequiredFields.put(CVConstants.PROPERTY_GRADE_TYPE, scan.getGradeType());
			scanRequiredFields.put(CVConstants.PROPERTY_LATITUDE, scan.getLatitude());
			scanRequiredFields.put(CVConstants.PROPERTY_LONGITUDE, scan.getLongitude());

			scanRequiredFields.put(CVConstants.PROPERTY_ADDRESS, scan.getAddress());
			scanRequiredFields.put(CVConstants.PROPERTY_HOUSE_NO, scan.getHouseNo());

			instaplanDto = scanToInstaplanDtoConvertor.mapScanDetailsToRoomsStructure(null, instaplanDto, scan, false);

			instaplanDto = scanToInstaplanDtoConvertor.mapDeficienciesToStructure(scan.getDeficiencies(), instaplanDto,
					scan);

			instaplanDto = scanToInstaplanDtoConvertor.mapRoomPhotosToStructure(scan.getRoomPhotos(), instaplanDto,
					scan);

			instaplanDto = scanToInstaplanDtoConvertor.mapPropertyUseToStructure(scan.getPropertyUse(), instaplanDto,
					scan);

			instaplanDto = scanToInstaplanDtoConvertor.mapFeaturesToStructureSmartTags(scan.getFeatures(), instaplanDto,
					scan);

			instaplanDto = scanToInstaplanDtoConvertor.mapPlumbingFixtureToStructure(scan.getPlumbingFixture(),
					instaplanDto, scan);

			instaplanDto = scanToInstaplanDtoConvertor.mapScanUpdatesToStructure(scan.getUpdates(), instaplanDto, scan);

			// roomPhotos,propertyUse,deficiencies,updates,features,plumbingFixture
		}

		Map<String, String> inspectionReportDetails = markerFileService
				.getInspectionReportDetails(paramDTO.getFolderPath(), paramDTO.getSessionId());

		for (InspectionReport report : reports) {

			if (inspectionReportDetails.containsKey(CVConstants.UTILITIES))
				instaplanDto = scanToInstaplanDtoConvertor
						.mapSiteUtilities(inspectionReportDetails.get(CVConstants.UTILITIES), instaplanDto);

			if (inspectionReportDetails.containsKey(CVConstants.DEFICIENCIES))
				instaplanDto = scanToInstaplanDtoConvertor.mapInspectReportDeficienciesToProperty(
						inspectionReportDetails.get(CVConstants.DEFICIENCIES), instaplanDto, scanRequiredFields);

			// location -> (no exterior/interior) -> InspectionReport.property.smartTags
			if (inspectionReportDetails.containsKey(CVConstants.LOCATION))
				instaplanDto = scanToInstaplanDtoConvertor.mapLocationToPropertySmartTags(
						inspectionReportDetails.get(CVConstants.LOCATION), instaplanDto, scanRequiredFields);

			// pdapi
			if (inspectionReportDetails.containsKey(CVConstants.PDAPIS))
				instaplanDto = scanToInstaplanDtoConvertor.mapPDAPIToStructureSmartTags(
						inspectionReportDetails.get(CVConstants.PDAPIS), instaplanDto, scanRequiredFields);
			// views

			if (inspectionReportDetails.containsKey(CVConstants.VIEWS))
				instaplanDto = scanToInstaplanDtoConvertor.mapViewsToStructureSmartTags(
						inspectionReportDetails.get(CVConstants.VIEWS), instaplanDto, scanRequiredFields);

			// updates
			if (inspectionReportDetails.containsKey(CVConstants.UPDATES))
				instaplanDto = scanToInstaplanDtoConvertor.mapUpdatesToStructureSmartTags(
						inspectionReportDetails.get(CVConstants.UPDATES), instaplanDto, scanRequiredFields);

			instaplanDto.setSubmittedDateAndTime(report.getSubmittedAt());

		}
		instaplanDto = setphotoJsonPath(instaplanDto);
		Map<String, String> map = new HashMap<>();
		List<CvOrder> cvOrders = cvOrderRepoImpl.getCvOrders(paramDTO);
		if (cvOrders != null && cvOrders.size() >= 1)
			map = setOrderMap(cvOrders.iterator().next(), map);
		else
			map = getCVOrderMap(map, scanRequiredFields);

		try {
			root = populateCVJson.generateJson(map, instaplanDto);
		} catch (Exception e) {
			log.error("generateCVReport: Exception: " + e.getMessage());
		}
		inspectionReportDto.setRoot(root);
		return inspectionReportDto;
	}

	private InstaplanDto setphotoJsonPath(InstaplanDto instaplanDto) {
		log.info("setphotoJsonPath ");
		// set property.smartTag.photoJsonPath
		if (!CollectionUtils.isEmpty(instaplanDto.getSmartTags()))
			for (SmartTagDto propertySmartTag : instaplanDto.getSmartTags()) {
				if (!CollectionUtils.isEmpty(propertySmartTag.getCv_photos()))
					for (PropertyCvPhoto cv_photo : propertySmartTag.getCv_photos()) {
						String jsonPath = "property.smartTags[" + instaplanDto.getSmartTags().indexOf(propertySmartTag)
								+ "]";
//						cv_photo.setPhotoJsonPath(jsonPath);
					}
			}

		int numofRooms = 0;
		int numofFloors = 0;
		// set property.structures.units.levels.rooms.smartTags
		if (!CollectionUtils.isEmpty(instaplanDto.getStructures()))
			for (StructureDto structure : instaplanDto.getStructures()) {
				if (!CollectionUtils.isEmpty(structure.getUnits()))
					for (UnitDto unit : structure.getUnits()) {
						if (!CollectionUtils.isEmpty(unit.getLevels()))
							for (LevelDto level : unit.getLevels()) {
								numofFloors = numofFloors + 1;
								if (!CollectionUtils.isEmpty(level.getCv_photos()))
									for (CvPhoto levelCvPhoto : level.getCv_photos()) {
										String levelCvPhotoJsonPath = "property.structures["
												+ instaplanDto.getStructures().indexOf(structure) + "]" + ".units["
												+ structure.getUnits().indexOf(unit) + "].levels["
												+ unit.getLevels().indexOf(level) + "]";
//										levelCvPhoto.setPhotoJsonPath(levelCvPhotoJsonPath);
									}

								if (!CollectionUtils.isEmpty(level.getRooms()))
									for (RoomDto room : level.getRooms()) {
										numofRooms = numofRooms + 1;
										if (!CollectionUtils.isEmpty(room.getSmartTags()))
											for (SmartTagDto smartTag : room.getSmartTags()) {
												if (!CollectionUtils.isEmpty(smartTag.getCv_photos()))
													for (PropertyCvPhoto cv_photo : smartTag.getCv_photos()) {
														String jsonPath = "property.structures["
																+ instaplanDto.getStructures().indexOf(structure) + "]"
																+ ".units[" + structure.getUnits().indexOf(unit)
																+ "].levels[" + unit.getLevels().indexOf(level)
																+ "].rooms[" + level.getRooms().indexOf(room)
																+ "].smartTags[" + room.getSmartTags().indexOf(smartTag)
																+ "]";
//														cv_photo.setPhotoJsonPath(jsonPath);
													}
											}
										// cv_photos inside room
										if (!CollectionUtils.isEmpty(room.getCv_photos()))
											for (CvPhoto roomCv_photo : room.getCv_photos()) {
												String jsonPath = "property.structures["
														+ instaplanDto.getStructures().indexOf(structure) + "]"
														+ ".units[" + structure.getUnits().indexOf(unit) + "].levels["
														+ unit.getLevels().indexOf(level) + "].rooms["
														+ level.getRooms().indexOf(room) + "]";
//												roomCv_photo.setPhotoJsonPath(jsonPath);
											}
									}
							}
					}
			}
		instaplanDto.setNumberOfRooms(numofRooms);
		instaplanDto.setNumberOfFloors(numofFloors);
		return instaplanDto;
	}

	private Map<String, String> setOrderMap(CvOrder cvOrder, Map<String, String> map) {
		log.info("setOrderMap");
		map.put("orderId", cvOrder.getOrderId());
		map.put("state", cvOrder.getState());
		map.put("city", cvOrder.getCity());
		map.put("county", cvOrder.getCounty());
		map.put("postalCode", cvOrder.getPostalCode());
		map.put("streetAddress", cvOrder.getStreetAddress());
		map.put("streetAddress2", cvOrder.getStreetAddress2());

		map.put("userID", cvOrder.getUserId());
		map.put("propertyDataCollectorType", cvOrder.getPropertyDataCollectorType());
		map.put("propertyDataCollectorName", cvOrder.getPropertyDataCollectorName());
		map.put("companyName", cvOrder.getCompanyName());

		map.put("firstName", cvOrder.getFirstName());
		map.put("lastName", cvOrder.getLastName());
		map.put("phone", cvOrder.getPdcPhone());
		map.put("email", cvOrder.getPdcEmail());

		map.put("pdc_state", cvOrder.getPdcState());
		map.put("pdc_city", cvOrder.getPdcCity());
		map.put("pdc_postalCode", cvOrder.getPdcPostalCode());
		map.put("pdc_streetAddress", cvOrder.getPdcStreetAddress());
		return map;
	}

	private Map<String, String> getCVOrderMap(Map<String, String> map, Map<String, String> detailsMap) {
		log.info("getCVOrderMap");

		String reportId = detailsMap.get("reportId");
		if (reportId != null)
			reportId = "CVN3D00" + reportId;
		else
			reportId = "CVN3D001";

		map.put("orderId", reportId);
		map.put("state", "UT");
		map.put("city", "North Ogden");
		map.put("county", "Weber");
		map.put("postalCode", "84414");
		map.put("streetAddress", detailsMap.get("address"));
		map.put("streetAddress2", detailsMap.get("houseNo"));

		map.put("userID", detailsMap.get("userID"));
		map.put("propertyDataCollectorType", "PHOTOGRAPHER");
		map.put("propertyDataCollectorName", "PDC Name");
		map.put("companyName", detailsMap.get("companyName"));

		map.put("firstName", detailsMap.get("firstName"));
		map.put("lastName", detailsMap.get("lastName"));
		map.put("phone", detailsMap.get("phone"));
		map.put("email", detailsMap.get("email"));

		map.put("pdc_state", "UT");
		map.put("pdc_city", "North Ogden");
		map.put("pdc_postalCode", "84414");
		map.put("pdc_streetAddress", "3033 N 425 E");

		return map;
	}
	private String getScanDetailsforCVReport(ParamDTO paramDTO,JSONObject addressJson) {
		String folderPath = paramDTO.getFolderPath();
		List<String> files= storjService.listObjects(bucketName,folderPath);
		JSONObject temp = addressJson;
		return "";
	}
	private String getInspectionReports(ParamDTO paramDTO) {
		int lastSlashIndex = paramDTO.getFolderPath().lastIndexOf('/');
		int secondLastSlashIndex = paramDTO.getFolderPath().lastIndexOf('/', lastSlashIndex - 1);
		String resultString="";
		if (lastSlashIndex != -1 && secondLastSlashIndex != -1) {
            resultString = paramDTO.getFolderPath().substring(0, secondLastSlashIndex + 1) 
                                + paramDTO.getFolderPath().substring(lastSlashIndex + 1);
            System.out.println("Modified String: " + resultString);
        } else {
            System.out.println("Not enough slashes in the string.");
        }
		List<String> files= storjService.listObjects(bucketName,resultString);
		return "";
	}

}
