package com.neuron.cv.service;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.neuron.cv.dto.InstaplanDto;
import com.neuron.cv.dto.LevelDto;
import com.neuron.cv.dto.RoomDto;
import com.neuron.cv.dto.SmartTagDto;
import com.neuron.cv.dto.StructureDto;
import com.neuron.cv.dto.UnitDto;
import com.neuron.cv.entity.AboveGrade;
import com.neuron.cv.entity.AdditionalDatum;
import com.neuron.cv.entity.Address;
import com.neuron.cv.entity.BelowGrade;
import com.neuron.cv.entity.CvAppraislManagementCompany;
import com.neuron.cv.entity.CvGpsCoordinates;
import com.neuron.cv.entity.CvLender;
import com.neuron.cv.entity.CvMetrics;
import com.neuron.cv.entity.CvPhoto;
import com.neuron.cv.entity.ElectricalService;
import com.neuron.cv.entity.Exterior;
import com.neuron.cv.entity.Features;
import com.neuron.cv.entity.FuelService;
import com.neuron.cv.entity.GpsCoordinates;
import com.neuron.cv.entity.Identification;
import com.neuron.cv.entity.InspectionReport;
import com.neuron.cv.entity.Interior;
import com.neuron.cv.entity.Level;
import com.neuron.cv.entity.Lot;
import com.neuron.cv.entity.Party;
import com.neuron.cv.entity.Property;
import com.neuron.cv.entity.PropertyAnalysis;
import com.neuron.cv.entity.PropertyCvPhoto;
import com.neuron.cv.entity.PropertySmartTag;
import com.neuron.cv.entity.Room;
import com.neuron.cv.entity.Root;
import com.neuron.cv.entity.ScanningInfo;
import com.neuron.cv.entity.SewerService;
import com.neuron.cv.entity.Site;
import com.neuron.cv.entity.SiteUtility;
import com.neuron.cv.entity.SmartTag;
import com.neuron.cv.entity.Structure;
import com.neuron.cv.entity.Unit;
import com.neuron.cv.entity.WaterService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PopulateCVJson {

  public Root generateJson(Map map, InstaplanDto instaplan) throws Exception {
    
    log.info("PopulateCVJson: generateJson: started");

    Root root = new Root();

    // InspectionReport
    // required["orderId","county","propertyDataCollectorName","propertyDataCollectorType"]
    InspectionReport inspectionReport = new InspectionReport();

    // Address: required["streetAddress","city","state","postalCode"]
    Address address = new Address();
    address = mapAddressFields(map, address);

    ArrayList<PropertyCvPhoto> addressCVPhotos = new ArrayList<PropertyCvPhoto>();
   // removing as of now.there are no values
   // PropertyCvPhoto cvPhoto = new PropertyCvPhoto();
    //cvPhoto.setPhotoType("OTHER");
    //addressCVPhotos.add(cvPhoto);

    address.setCv_photos(addressCVPhotos);

    // Identification: required["cv_gpsCoordinates"]
    Identification identification = Identification.builder().build();
    GpsCoordinates gpsCoordinates = new GpsCoordinates();
    if (instaplan != null && instaplan.getGpsCoordinates() != null) {
      gpsCoordinates.setLatitude(instaplan.getGpsCoordinates().getLatitude());
      gpsCoordinates.setLongitude(instaplan.getGpsCoordinates().getLongitude());
    }

    // CvGpsCoordinates: required["latitude","longitude"]
    CvGpsCoordinates cvGpsCoordinates = new CvGpsCoordinates();
    if (instaplan != null && instaplan.getCv_gpsCoordinates() != null) {
      cvGpsCoordinates.setLatitude(instaplan.getCv_gpsCoordinates().getLatitude());
      cvGpsCoordinates.setLongitude(instaplan.getCv_gpsCoordinates().getLongitude());
    }

    identification.setGpsCoordinates(gpsCoordinates);
    identification.setCv_gpsCoordinates(cvGpsCoordinates);

    PropertyAnalysis propertyAnalysis = new PropertyAnalysis();
    Site site = Site.builder().build();
    Lot lot = new Lot();

    ArrayList<ElectricalService> electricalServices = new ArrayList<ElectricalService>();
    // ElectricalService electricalService = new ElectricalService();
    if (!CollectionUtils.isEmpty(instaplan.getElectricalServices()))
      electricalServices.addAll(instaplan.getElectricalServices());

    ArrayList<SewerService> sewerServices = new ArrayList<>();
    // SewerService sewerService = new SewerService();
    if (!CollectionUtils.isEmpty(instaplan.getSewerServices()))
      sewerServices.addAll(instaplan.getSewerServices());

    ArrayList<WaterService> waterServices = new ArrayList<>();
    // WaterService waterService = new WaterService();
    if (!CollectionUtils.isEmpty(instaplan.getWaterServices()))
      waterServices.addAll(instaplan.getWaterServices());

    ArrayList<FuelService> fuelServices = new ArrayList<>();
    // FuelService fuelService = new FuelService();
    if (!CollectionUtils.isEmpty(instaplan.getFuelServices()))
      fuelServices.addAll(instaplan.getFuelServices());

    SiteUtility siteUtility =
        SiteUtility.builder().electricalServices(electricalServices).sewerServices(sewerServices)
            .waterServices(waterServices).fuelServices(fuelServices).build();

    site.setLot(lot);
    site.setSiteUtility(siteUtility);

    ArrayList<Structure> structures = new ArrayList<Structure>();
    if (!CollectionUtils.isEmpty(instaplan.getStructures())) {
      for (StructureDto structureDto : instaplan.getStructures()) {
        Structure structure = new Structure();
        structure.setCv_Label(structureDto.getCv_Label());
        structure.setStructureType(structureDto.getStructureType());
        ArrayList<Unit> units = new ArrayList<Unit>();
        if (!CollectionUtils.isEmpty(structureDto.getUnits())) {
          for (UnitDto unitDto : structureDto.getUnits()) {
            Unit unit = new Unit();
            unit.setCv_Label(unitDto.getCv_Label());
            unit.setUnitType(unitDto.getUnitType());
            ArrayList<Level> levels = new ArrayList<Level>();
            if (!CollectionUtils.isEmpty(unitDto.getLevels())) {
              for (LevelDto levelDto : unitDto.getLevels()) {
                Level level = new Level();
                level.setLevelId(levelDto.getLevelId());
                level.setCv_Label(levelDto.getCv_Label());
                level.setCv_finishedFloorArea(levelDto.getCv_finishedFloorArea());
                level.setCv_nonGlaFinishedFloorArea(levelDto.getCv_nonGlaFinishedFloorArea());
                level.setCv_glaFinishedFloorArea(levelDto.getCv_glaFinishedFloorArea());
                level.setCv_finishedFloorAreaPercentage(
                    levelDto.getCv_finishedFloorAreaPercentage());
                level.setCv_unfinishedFloorArea(levelDto.getCv_unfinishedFloorArea());
                level.setCv_unfinishedFloorAreaPercentage(
                    levelDto.getCv_unfinishedFloorAreaPercentage());
                level.setCv_grossFloorArea(levelDto.getCv_grossFloorArea());
                level.setCv_grossLivingArea(levelDto.getCv_grossLivingArea());
                level.setCv_garageArea(levelDto.getCv_garageArea());

                level.setFinishedArea(levelDto.getFinishedArea());
                level.setGla(levelDto.getGla());
                level.setNetArea(levelDto.getNetArea());
                level.setLivableArea(levelDto.getLivableArea());
                level.setUnfinishedArea(levelDto.getUnfinishedArea());
                level.setTotalArea(levelDto.getTotalArea());

                level.setLevelName(levelDto.getLevelName());
                
                ArrayList<CvPhoto> levelCVPhotos = new ArrayList<>();
                if (!CollectionUtils.isEmpty(levelDto.getCv_photos())) {
                  levelCVPhotos.addAll(levelDto.getCv_photos());
                }
                level.setCv_photos(levelCVPhotos);
                
                ArrayList<Room> rooms = new ArrayList<Room>();
                if (!CollectionUtils.isEmpty(levelDto.getRooms())) {
                  for (RoomDto roomDto : levelDto.getRooms()) {
                    Room room = new Room();
                    room.setCv_Label(roomDto.getCv_Label());
                    room.setCeiling(roomDto.getCeiling());
                    room.setIsOutsideArea(roomDto.getIsOutsideArea());
                    room.setRoomId(roomDto.getRoomId());
                    room.setRoomName(roomDto.getRoomName());
                    room.setPosition(roomDto.getPosition());
                    room.setConnections(roomDto.getConnections());
                    room.setCv_photos(roomDto.getCv_photos());
                    room.setIsGarage(roomDto.getIsGarage());
                    ArrayList<SmartTag> roomSmartTags = new ArrayList<SmartTag>();
                    if (!CollectionUtils.isEmpty(roomDto.getSmartTags())) {
                      for (SmartTagDto rmSmartTagDto : roomDto.getSmartTags()) {
                        SmartTag roomSmartTag = new SmartTag();
                        roomSmartTag.setVisible(rmSmartTagDto.isVisible());
                        roomSmartTag.setTagType(rmSmartTagDto.getTagType());
                        roomSmartTag.setSubCategory(rmSmartTagDto.getSubCategory());
                        roomSmartTag.setTitle(rmSmartTagDto.getTitle());
                        roomSmartTag.setGroupName(rmSmartTagDto.getGroupName());
                        roomSmartTag.setSmartTagObjectId(rmSmartTagDto.getSmartTagObjectId());
                        roomSmartTag.setCreatedDate(rmSmartTagDto.getCreatedDate());
                        roomSmartTag.setHasPhotos(rmSmartTagDto.isHasPhotos());
                        roomSmartTag.setCustom(rmSmartTagDto.isCustom());
                        if (!CollectionUtils.isEmpty(rmSmartTagDto.getCv_photos())) {
                          if (CollectionUtils.isEmpty(roomSmartTag.getCv_photos()))
                            roomSmartTag.setCv_photos(new ArrayList());
                          for (PropertyCvPhoto rmcv_photo : rmSmartTagDto.getCv_photos()) {
                            CvPhoto cvphoto = new CvPhoto();
                            cvphoto.setPhotoDescription(rmcv_photo.getPhotoDescription());
                            cvphoto.setPhotoType(rmcv_photo.getPhotoType());
                            cvphoto.setPhotoLatitude(rmcv_photo.getPhotoLatitude());
                            cvphoto.setPhotoLongitude(rmcv_photo.getPhotoLongitude());
                            cvphoto.setPhotoImgType(rmcv_photo.getPhotoImgType());
                            cvphoto.setPhotoTimeSpan(rmcv_photo.getPhotoTimeSpan());
                            cvphoto.setPhotoJsonPath(rmcv_photo.getPhotoJsonPath());
                            cvphoto.setPhotoFileURL(rmcv_photo.getPhotoFileURL());
                            roomSmartTag.getCv_photos().add(cvphoto);
                          }
                          roomSmartTags.add(roomSmartTag);
                        }
                      }
                    }
                    room.setSmartTags(roomSmartTags);
                    rooms.add(room);
                  }
                }
                
                level.setRooms(rooms);
                levels.add(level);
              }
            }
            unit.setLevels(levels);
            units.add(unit);
          }
        }
        structure.setUnits(units);
        structures.add(structure);
      }
    }
    Interior interior = new Interior();
    interior.setFloorLivableArea(instaplan.getFloorLivableArea());
    if (instaplan.getInterior() != null) {
      interior.setFloorArea(instaplan.getInterior().getFloorArea());
      interior.setVolumen(instaplan.getInterior().getVolumen());
      interior.setWallArea(instaplan.getInterior().getWallArea());
      interior.setNetArea(instaplan.getInterior().getNetArea());
      interior.setWallFootprintArea(instaplan.getInterior().getWallFootprintArea());
      interior.setGla(instaplan.getInterior().getGla());
      interior.setFinishedArea(instaplan.getInterior().getFinishedArea());
      interior.setUnfinishedArea(instaplan.getInterior().getUnfinishedArea());
      interior.setTotalArea(instaplan.getInterior().getTotalArea());
    }

    Exterior exterior = new Exterior();

    if (instaplan.getExterior() != null) {
      exterior.setFloorArea(instaplan.getExterior().getFloorArea());
      exterior.setNetArea(instaplan.getInterior().getNetArea());
    }

    Features features = new Features();

    CvMetrics cvMetrics = new CvMetrics();
    AboveGrade aboveGrade = new AboveGrade();
    BelowGrade belowGrade = new BelowGrade();
    cvMetrics.setAboveGrade(aboveGrade);
    cvMetrics.setBelowGrade(belowGrade);

    ArrayList<PropertySmartTag> propertysmartTags = new ArrayList<PropertySmartTag>();
    if (!CollectionUtils.isEmpty(instaplan.getSmartTags())) {
      for (SmartTagDto smartTagDto : instaplan.getSmartTags()) {
        PropertySmartTag smartTag = new PropertySmartTag();

        smartTag.setVisible(smartTagDto.isVisible());
        smartTag.setTagType(smartTagDto.getTagType());
        smartTag.setSubCategory(smartTagDto.getSubCategory());
        smartTag.setTitle(smartTagDto.getTitle());
        smartTag.setGroupName(smartTagDto.getGroupName());
        smartTag.setSmartTagObjectId(smartTagDto.getSmartTagObjectId());
        smartTag.setCreatedDate(smartTagDto.getCreatedDate());
        smartTag.setHasPhotos(smartTagDto.isHasPhotos());
        smartTag.setCustom(smartTagDto.isCustom());

        ArrayList<PropertyCvPhoto> smartTagCVPhotos = new ArrayList<>();
        if (CollectionUtils.isEmpty(smartTagDto.getCv_photos())) {
          PropertyCvPhoto smartTagCVPhoto = new PropertyCvPhoto();
          smartTagCVPhotos.add(smartTagCVPhoto);
        } else {
          smartTagCVPhotos.addAll(smartTagDto.getCv_photos());
        }
        smartTag.setCv_photos(smartTagCVPhotos);

        ArrayList<AdditionalDatum> additionalData = new ArrayList<>();
        AdditionalDatum additionalDatum = new AdditionalDatum();
        additionalData.add(additionalDatum);
        smartTag.setAdditionalData(additionalData);
        propertysmartTags.add(smartTag);
      }
    }



    // Property: required["address"]
    Property property = Property.builder().address(address).build();
    // property.setAddress(address);
    property.setIdentification(identification);
    property.setPropertyAnalysis(propertyAnalysis);
    property.setSite(site);
    property.setStructures(structures);
    property.setInterior(interior);
    property.setExterior(exterior);
    property.setFeatures(features);
    property.setCv_Metrics(cvMetrics);
    property.setSmartTags(propertysmartTags);

    property.setGla(instaplan.getGla());
    property.setFinishedArea(instaplan.getFinishedArea());
    property.setTotalArea(instaplan.getTotalArea());
    property.setUnfinishedArea(instaplan.getUnfinishedArea());
    property.setNetArea(instaplan.getNetArea());
    property.setNumberOfFloors(instaplan.getNumberOfFloors());
    property.setNumberOfRooms(instaplan.getNumberOfRooms());



    ScanningInfo scanningInfo = new ScanningInfo();
    scanningInfo.setScannedDate(instaplan.getScannedDate());
    scanningInfo.setSubmittedDateAndTime(instaplan.getSubmittedDateAndTime());
    scanningInfo.setFinishedDateAndTime(instaplan.getFinishedDateAndTime());
    scanningInfo.setFirstSpinCapturedAt(instaplan.getFirstSpinCapturedAt());
    scanningInfo.setLastSpinCapturedAt(instaplan.getLastSpinCapturedAt());



    CvLender cvLender = new CvLender();
    CvAppraislManagementCompany cvAppraislManagementCompany = new CvAppraislManagementCompany();

    ArrayList<Party> parties = new ArrayList<Party>();
    Party party = new Party();
    party.setCredentialType("OTHER");

    parties.add(mapPartyFields(map, party));

    // setting inspectionReport fields
    inspectionReport.setProperty(property);
    inspectionReport.setScanningInfo(scanningInfo);
    inspectionReport.setCv_lender(cvLender);
    inspectionReport.setCv_appraislManagementCompany(cvAppraislManagementCompany);
    inspectionReport.setParties(parties);

    // final setting
    root.setInspectionReport(mapInspectionReportFields(map, inspectionReport));

    return root;
  }

  // Address: required["streetAddress","city","state","postalCode"]
  private Address mapAddressFields(Map map, Address _address) {
    log.info("PopulateCVJson: mapAddressFields: started");
    Set<String> keys = null;
    if (map != null) {
      keys = map.keySet();
      if (!keys.isEmpty()) {
        for (String key : keys) {
          if (key.equalsIgnoreCase("streetAddress"))
            _address.setStreetAddress(getValueFromMap(map, "streetAddress"));
          if (key.equalsIgnoreCase("city"))
            _address.setCity(getValueFromMap(map, "city"));
          if (key.equalsIgnoreCase("state"))
            _address.setState(getValueFromMap(map, "state"));
          if (key.equalsIgnoreCase("postalCode"))
            _address.setPostalCode(getValueFromMap(map, "postalCode"));
          if (key.equalsIgnoreCase("streetAddress2"))
            _address.setStreetAddress2(getValueFromMap(map, "streetAddress2"));
        }
      }
    }
    return _address;
  }

  // Party
  private Party mapPartyFields(Map map, Party party) {
    log.info("PopulateCVJson: mapPartyFields: started");
    Set<String> keys = null;

    if (map != null) {
      keys = map.keySet();
      if (!keys.isEmpty()) {
        for (String key : keys) {
          if (key.equalsIgnoreCase("userID"))
            party.setUserId(getValueFromMap(map, "userID"));
          if (key.equalsIgnoreCase("firstName"))
            party.setFirstName(getValueFromMap(map, "firstName"));
          if (key.equalsIgnoreCase("lastName"))
            party.setLastName(getValueFromMap(map, "lastName"));
          if (key.equalsIgnoreCase("propertyDataCollectorType"))
            party.setRoleType(getValueFromMap(map, "propertyDataCollectorType"));
          if (key.equalsIgnoreCase("companyName"))
            party.setCompanyName(getValueFromMap(map, "companyName"));
          if (key.equalsIgnoreCase("pdc_streetAddress"))
            party.setStreetAddress(getValueFromMap(map, "pdc_streetAddress"));
          if (key.equalsIgnoreCase("pdc_city"))
            party.setCity(getValueFromMap(map, "pdc_city"));
          if (key.equalsIgnoreCase("pdc_state"))
            party.setState(getValueFromMap(map, "pdc_state"));
          if (key.equalsIgnoreCase("pdc_postalCode"))
            party.setPostalCode(getValueFromMap(map, "pdc_postalCode"));
          if (key.equalsIgnoreCase("phone"))
            party.setPhone(getValueFromMap(map, "phone"));
          if (key.equalsIgnoreCase("email"))
            party.setEmail(getValueFromMap(map, "email"));
          if (key.equalsIgnoreCase("cv_state"))
            party.setState(getValueFromMap(map, "cv_state"));
          if (key.equalsIgnoreCase("cv_postalCode"))
            party.setPostalCode(getValueFromMap(map, "cv_postalCode"));
          // UserId,propertyDataCollectorType,firstName,lastName,companyName,streetAddress,city,state,postalCode,phone,email
        }
      }
    }
    return party;
  }

  // InspectionReport required: orderId,county,propertyDataCollectorName,propertyDataCollectorType
  private InspectionReport mapInspectionReportFields(Map map, InspectionReport party) {
    log.info("PopulateCVJson: mapInspectionReportFields: started");
    Set<String> keys = null;

    if (map != null) {
      keys = map.keySet();
      if (!keys.isEmpty()) {
        for (String key : keys) {
          if (key.equalsIgnoreCase("orderId"))
            party.setOrderId(getValueFromMap(map, "orderId"));
          if (key.equalsIgnoreCase("county"))
            party.setCounty(getValueFromMap(map, "county"));
          if (key.equalsIgnoreCase("propertyDataCollectorName"))
            party.setPropertyDataCollectorName(getValueFromMap(map, "propertyDataCollectorName"));
          if (key.equalsIgnoreCase("propertyDataCollectorType"))
            party.setPropertyDataCollectorType(getValueFromMap(map, "propertyDataCollectorType"));
        }
      }
    }
    return party;
  }

  private String getValueFromMap(Map map, String key) {
    Object obj = map.get(key);
    if (obj != null)
      return obj.toString();
    return null;
  }

  public String createJsonFileFromObject(Root root, String fileName) throws Exception {
    log.info("PopulateCVJson: createJsonFileFromObject: started");
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    JavaTimeModule module = new JavaTimeModule();
    mapper.registerModule(module);

    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setSerializationInclusion(Include.NON_EMPTY);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    mapper.setDateFormat(df);
    String postJson = mapper.writeValueAsString(root);
    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
    mapper.writeValue(fileOutputStream, root);
    fileOutputStream.close();
    return postJson;
  }
}
