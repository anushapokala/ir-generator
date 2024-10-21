package com.neuron.cv.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.constants.CVLookUpDetails;
import com.neuron.cv.dbentity.Scan;
import com.neuron.cv.dto.InstaplanDto;
import com.neuron.cv.dto.LevelDto;
import com.neuron.cv.dto.RoomDto;
import com.neuron.cv.dto.StructureDto;
import com.neuron.cv.dto.UnitDto;
import com.neuron.cv.entity.CvGpsCoordinates;
import com.neuron.cv.entity.CvPhoto;
import com.neuron.cv.entity.ElectricalService;
import com.neuron.cv.entity.GasService;
import com.neuron.cv.entity.GpsCoordinates;
import com.neuron.cv.entity.SewerService;
import com.neuron.cv.entity.WaterService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScanToInstaplanDtoConvertor {

  @Value("${storj.bucketName}")
  private String bucketName;

  @Autowired
  public MetricsCalucatorService metricsCalucatorService;
  @Autowired
  public ScanToInstaplanDtoCommonConvertor scanToInstaplanDtoCommonConvertor;


  public InstaplanDto mapSiteUtilities(String utilities, InstaplanDto instaplanDto) {
    log.info("mapSiteUtilities: " + LocalDateTime.now());
    try {
      if (utilities != null && !utilities.isBlank()) {
        JSONObject utilitiesObj = new JSONObject(utilities);
        Iterator<?> utilitiesKeys = utilitiesObj.keys();
        while (utilitiesKeys.hasNext()) {
          String utilitiesKey = (String) utilitiesKeys.next();
          JSONArray array = (JSONArray) utilitiesObj.get(utilitiesKey);
          for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);

            switch (object.get(CVConstants.SERVICE_NAME).toString()) {
              case CVConstants.ELECTRICAL_SERVICES -> {
                if (CollectionUtils.isEmpty(instaplanDto.getElectricalServices()))
                  instaplanDto.setElectricalServices(new ArrayList<>());
                instaplanDto.getElectricalServices()
                .add(new ElectricalService(object.getString(CVConstants.SERVICE_TYPE).toString()));
              }

              case CVConstants.SEWER_SERVICES -> {
                if (CollectionUtils.isEmpty(instaplanDto.getSewerServices()))
                  instaplanDto.setSewerServices(new ArrayList<>());
                instaplanDto.getSewerServices()
                    .add(new SewerService(object.getString(CVConstants.SERVICE_TYPE).toString()));
              }

              case CVConstants.WATER_SERVICES -> {
                if (CollectionUtils.isEmpty(instaplanDto.getWaterServices()))
                  instaplanDto.setWaterServices(new ArrayList<>());
                instaplanDto.getWaterServices()
                    .add(new WaterService(object.getString(CVConstants.SERVICE_TYPE).toString()));
              }

              case CVConstants.GAS_SERVICES -> {
                if (CollectionUtils.isEmpty(instaplanDto.getGasServices()))
                  instaplanDto.setGasServices(new ArrayList<>());
                instaplanDto.getGasServices()
                    .add(new GasService(object.getString(CVConstants.SERVICE_TYPE).toString()));
              }
              default -> throw new IllegalArgumentException(
                  "Unexpected value: " + object.get(CVConstants.SERVICE_NAME));
            };
          }
        }
      }
    } catch (Exception e) {
      log.error("mapSiteUtilities: Error: ", e.getMessage());
    }
    return instaplanDto;
  }

  public InstaplanDto mapDeficienciesToStructure(String deficiencies, InstaplanDto instaplanDto,
      Scan scan) {
    try {
      log.info("mapDeficienciesToStructure: " + LocalDateTime.now());
      List<RoomDto> roomList = scanToInstaplanDtoCommonConvertor
          .mapScanPropertiestoStrucuture(deficiencies, scan, CVConstants.MID_SCAN_DEFICIENCIES);
      instaplanDto = mapScanDetailsToRoomsStructure(roomList, instaplanDto, scan, true);
    } catch (Exception e) {
      log.error("mapDeficienciesToStructure: Exception: " + e.getMessage());
    }
    return instaplanDto;
  }

  public InstaplanDto mapPropertyUseToStructure(String propertyUse, InstaplanDto instaplanDto,
      Scan scan) {
    try {
      log.info("mapPropertyUseToStructure");

      List<RoomDto> roomList = scanToInstaplanDtoCommonConvertor
          .mapScanPropertiestoStrucuture(propertyUse, scan, CVConstants.MID_SCAN_PROPERTY_USE);
      instaplanDto = mapScanDetailsToRoomsStructure(roomList, instaplanDto, scan, true);

    } catch (Exception e) {
      log.error("mapPropertyUseToStructure: Exception: " + e.getMessage());
    }
    return instaplanDto;
  }


  public InstaplanDto mapScanDetailsToRoomsStructure(List<RoomDto> rooms, InstaplanDto instaplanDto,
      Scan scan, boolean isfromProperties) {
    log.info("mapScanDetailsToRoomsStructure: " + LocalDateTime.now());
    if (isfromProperties && CollectionUtils.isEmpty(rooms))
      return instaplanDto;
    if (CollectionUtils.isEmpty(instaplanDto.getStructures())) {
      instaplanDto.setStructures(new ArrayList<>());
      instaplanDto = intializateStructure(scan, instaplanDto, rooms);
    } else {
      boolean isStructureMatched = false;
      for (StructureDto Structure : instaplanDto.getStructures()) {
        String StructureType = getStructureType(scan);
        if (Structure.getStructureType().equalsIgnoreCase(StructureType)) {
          // as of now units are not checking considering one unit per structure
          UnitDto unit = Structure.getUnits().get(0);
          String levelName = scan.getGradeType() + " " + scan.getGradeLevel();
          boolean islevelMatched = false;
          isStructureMatched = true;
          for (LevelDto level : unit.getLevels()) {
            if (level.getLevelName().equalsIgnoreCase(levelName)) {
              if (!level.isfloorCalAPICalled)
                level = metricsCalucatorService.getFloorCalcProps(scan, level);
              islevelMatched = true;
              if (!CollectionUtils.isEmpty(rooms))
                for (RoomDto CurrentRoom : rooms) {
                  boolean isSmartTagSet = false;
                  for (RoomDto room : level.getRooms()) {
                    if (room.getRoomName() != null && CurrentRoom.getRoomName() != null
                        && room.getRoomName().equalsIgnoreCase(CurrentRoom.getRoomName())) {
                      if (CollectionUtils.isEmpty(room.getSmartTags()))
                        room.setSmartTags(new ArrayList<>());
                      if (!CollectionUtils.isEmpty(CurrentRoom.getSmartTags()))
                        room.getSmartTags().addAll(CurrentRoom.getSmartTags());

                      if (CollectionUtils.isEmpty(room.getCv_photos()))
                        room.setCv_photos(new ArrayList<>());
                      if (!CollectionUtils.isEmpty(CurrentRoom.getCv_photos()))
                        room.getCv_photos().addAll(CurrentRoom.getCv_photos());

                      isSmartTagSet = true;

                    } else if (room.getRoomName() == null && CurrentRoom.getRoomName() == null) {
                      if (CollectionUtils.isEmpty(room.getSmartTags()))
                        room.setSmartTags(new ArrayList<>());
                      if (!CollectionUtils.isEmpty(CurrentRoom.getSmartTags()))
                        room.getSmartTags().addAll(CurrentRoom.getSmartTags());

                      if (CollectionUtils.isEmpty(room.getCv_photos()))
                        room.setCv_photos(new ArrayList<>());
                      if (!CollectionUtils.isEmpty(CurrentRoom.getCv_photos()))
                        room.getCv_photos().addAll(CurrentRoom.getCv_photos());
                      isSmartTagSet = true;
                    }
                  }
                  if (islevelMatched && !isSmartTagSet) {
                    level.getRooms().add(CurrentRoom);
                    isSmartTagSet = true;
                  }
                }

            }
          }
          if (!islevelMatched) {
            LevelDto levelDto = setLevelDto(scan, rooms);
            Structure.getUnits().get(0).getLevels().add(levelDto);

          }
        }

      }
      if (!isStructureMatched) {
        instaplanDto = intializateStructure(scan, instaplanDto, rooms);
      }
    }
    return instaplanDto;
  }

  public LevelDto setLevelDto(Scan scan, List<RoomDto> rooms) {
    LevelDto levelDto = new LevelDto();
    levelDto.setCv_Label(scan.getGradeType() + " " + scan.getGradeLevel());
    levelDto.setLevelName(levelDto.getCv_Label());
    levelDto = metricsCalucatorService.getFloorCalcProps(scan, levelDto);
    levelDto.setRooms(new ArrayList<>());
    if (!CollectionUtils.isEmpty(rooms))
      levelDto.getRooms().addAll(rooms);
    levelDto.setCv_photos(new ArrayList<>());
    CvPhoto cvPhoto = new CvPhoto();
    if (scan.getLongitude() != null) {
      cvPhoto.setPhotoLongitude(scan.getLongitude());
    }
    if (scan.getLatitude() != null) {
      cvPhoto.setPhotoLatitude(scan.getLatitude());
    }
    cvPhoto.setPhotoType("SKETCH");
    cvPhoto.setPhotoDescription("Image Of Floorplan");
    cvPhoto.setPhotoDescription("Image Of Floorplan");
    cvPhoto.setPhotoFileURL("/instaplan/floor-plan/2d-floor-plan.jpg");
    Date scanDate = Date.from(scan.getCreatedAt());
    cvPhoto.setPhotoTimeSpan(scanDate.getTime());
    levelDto.getCv_photos().add(cvPhoto);
    return levelDto;
  }

  public String getStructureType(Scan scan) {
    if (scan.getStructure() != null && !scan.getStructure().isEmpty()
        && scan.getStructure().equalsIgnoreCase(CVConstants.PRIMARY))
      return CVConstants.DWELLING;
    else if (scan.getStructure() != null && !scan.getStructure().isEmpty()
        && scan.getStructure().equalsIgnoreCase(CVConstants.SECONDARY)) {
      if (scan.getStructureType() != null && !scan.getStructureType().isEmpty())
        return scan.getStructureType();
      else
        return CVConstants.NA;
    }
    return CVConstants.NA;
  }

  public StructureDto setStructureType(StructureDto structure, Scan scan) {
    if (scan.getStructure() != null && !scan.getStructure().isEmpty()
        && scan.getStructure().equalsIgnoreCase(CVConstants.PRIMARY))
      structure.setCv_Label(CVConstants.DWELLING);
    else if (scan.getStructure() != null && !scan.getStructure().isEmpty()
        && scan.getStructure().equalsIgnoreCase(CVConstants.SECONDARY))
      structure.setCv_Label(scan.getStructureType());

    if (structure.getCv_Label() != null && structure.getCv_Label().contains(CVConstants.DWELLING))
      structure.setStructureType(CVConstants.DWELLING);

    else if (scan.getStructureType() != null && !scan.getStructureType().isEmpty())
      structure.setStructureType(scan.getStructureType());
    else
      structure.setStructureType(CVConstants.NA);

    return structure;
  }

  private InstaplanDto intializateStructure(Scan scan, InstaplanDto instaplanDto,
      List<RoomDto> rooms) {
    log.info("intializateStructure: " + LocalDateTime.now());
    // as of now considering structure having single unit,single level
    StructureDto structure = new StructureDto();
    if (scan != null) {
      setStructureType(structure, scan);
      structure.setUnits(new ArrayList<>());
      UnitDto unitDto = new UnitDto();
      unitDto.setCv_Label(CVConstants.NA);
      unitDto.setUnitType(CVConstants.NA);
      unitDto.setLevels(new ArrayList<>());

      LevelDto levelDto = setLevelDto(scan, rooms);
      unitDto.getLevels().add(levelDto);
      structure.getUnits().add(unitDto);
      instaplanDto.getStructures().add(structure);

    }
    return instaplanDto;
  }



  public InstaplanDto setScanLevelProps(Scan scan, InstaplanDto instaplanDto) {

    log.info("setScanLevelProps");

    GpsCoordinates gpsCoordinates = new GpsCoordinates();
    CvGpsCoordinates cvGpsCoordinates = new CvGpsCoordinates();

    if (scan.getLatitude() != null && !scan.getLatitude().isBlank())
      gpsCoordinates.setLatitude(Double.parseDouble(scan.getLatitude()));
    if (scan.getLongitude() != null && !scan.getLongitude().isBlank())
      gpsCoordinates.setLongitude(Double.parseDouble(scan.getLongitude()));


    if (scan.getLatitude() != null && !scan.getLatitude().isBlank())
      cvGpsCoordinates.setLatitude(Double.parseDouble(scan.getLatitude()));

    if (scan.getLongitude() != null && !scan.getLongitude().isBlank())
      cvGpsCoordinates.setLongitude(Double.parseDouble(scan.getLongitude()));

    instaplanDto.setGpsCoordinates(gpsCoordinates);
    instaplanDto.setCv_gpsCoordinates(cvGpsCoordinates);

    return instaplanDto;
  }

  // location -> (no exterior/interior) -> InspectionReport.property.smartTags
  public InstaplanDto mapLocationToPropertySmartTags(String location, InstaplanDto instaplanDto,
      Map<String, String> scanRequiredFields) {
    log.info("mapLocationToPropertySmartTags");
    return scanToInstaplanDtoCommonConvertor.mapInspectReportPropertiesToPropertyField(location,
        instaplanDto, scanRequiredFields, CVConstants.POST_SCAN_LOCATION);
  }


  public InstaplanDto mapPDAPIToStructureSmartTags(String pdapi, InstaplanDto instaplanDto,
      Map<String, String> scanRequiredFields) {
    return scanToInstaplanDtoCommonConvertor.mapInspectReportPropertiesToPropertyField(pdapi,
        instaplanDto, scanRequiredFields, CVConstants.POST_SCAN_PDAPI);
  }

  public InstaplanDto mapViewsToStructureSmartTags(String views, InstaplanDto instaplanDto,
      Map<String, String> scanRequiredFields) {
    return scanToInstaplanDtoCommonConvertor.mapInspectReportPropertiesToPropertyField(views,
        instaplanDto, scanRequiredFields, CVConstants.POST_SCAN_VIEWS);
  }

  public InstaplanDto mapUpdatesToStructureSmartTags(String updates, InstaplanDto instaplanDto,
      Map<String, String> scanRequiredFields) {
    return scanToInstaplanDtoCommonConvertor.mapInspectReportPropertiesToPropertyField(updates,
        instaplanDto, scanRequiredFields, CVConstants.POST_SCAN_UPDATES);
  }

  private InstaplanDto mapRoomPhotosToRoomsStructure(List<RoomDto> rooms, InstaplanDto instaplanDto,
      Scan scan) {
    log.info("mapRoomPhotosToRoomsStructure: " + LocalDateTime.now());
    if (CollectionUtils.isEmpty(rooms))
      return instaplanDto;
    if (CollectionUtils.isEmpty(instaplanDto.getStructures())) {
      instaplanDto.setStructures(new ArrayList<>());
      instaplanDto = intializateStructure(scan, instaplanDto, rooms);
    } else {
      boolean isStructureMatched = false;
      for (StructureDto Structure : instaplanDto.getStructures()) {
        String StructureType = getStructureType(scan);
        if (Structure.getStructureType().equalsIgnoreCase(StructureType)) {
          // as of now units are not checking considering one unit per structure
          UnitDto unit = Structure.getUnits().get(0);
          String levelName = scan.getGradeType() + " " + scan.getGradeLevel();
          boolean islevelMatched = false;
          isStructureMatched = true;
          for (LevelDto level : unit.getLevels()) {
            if (level.getLevelName().equalsIgnoreCase(levelName)) {
              if (!level.isfloorCalAPICalled)
                level = metricsCalucatorService.getFloorCalcProps(scan, level);
              islevelMatched = true;
              for (RoomDto CurrentRoom : rooms) {
                boolean isSmartTagSet = false;
                for (RoomDto room : level.getRooms()) {
                  if (room.getRoomName() != null && CurrentRoom.getRoomName() != null
                      && room.getRoomName().equalsIgnoreCase(CurrentRoom.getRoomName())) {

                    if (CollectionUtils.isEmpty(room.getCv_photos()))
                      room.setCv_photos(new ArrayList<>());
                    if (!CollectionUtils.isEmpty(CurrentRoom.getCv_photos()))
                      room.getCv_photos().addAll(CurrentRoom.getCv_photos());

                    isSmartTagSet = true;

                  } else if (room.getRoomName() == null && CurrentRoom.getRoomName() == null) {

                    if (CollectionUtils.isEmpty(room.getCv_photos()))
                      room.setCv_photos(new ArrayList<>());
                    if (!CollectionUtils.isEmpty(CurrentRoom.getCv_photos()))
                      room.getCv_photos().addAll(CurrentRoom.getCv_photos());
                    isSmartTagSet = true;
                  }
                }
                if (islevelMatched && !isSmartTagSet) {
                  level.getRooms().add(CurrentRoom);
                  isSmartTagSet = true;
                }
              }

            }
          }
          if (!islevelMatched) {
            LevelDto levelDto = new LevelDto();
            levelDto.setCv_Label(scan.getGradeType() + " " + scan.getGradeLevel());
            levelDto.setLevelName(levelDto.getCv_Label());
            levelDto = metricsCalucatorService.getFloorCalcProps(scan, levelDto);
            levelDto.setRooms(new ArrayList<>());
            levelDto.getRooms().addAll(rooms);
            levelDto.setCv_photos(new ArrayList<>());
            CvPhoto cvPhoto = new CvPhoto();
            if (scan.getLongitude() != null) {
              cvPhoto.setPhotoLongitude(scan.getLongitude());
            }
            if (scan.getLatitude() != null) {
              cvPhoto.setPhotoLatitude(scan.getLatitude());
            }
            cvPhoto.setPhotoType("SKETCH");
            cvPhoto.setPhotoDescription("Image Of Floorplan");
            cvPhoto.setPhotoDescription("Image Of Floorplan");
            cvPhoto.setPhotoFileURL("/instaplan/floor-plan/2d-floor-plan.jpg");
            Date scanDate = Date.from(scan.getCreatedAt());
            cvPhoto.setPhotoTimeSpan(scanDate.getTime());
            levelDto.getCv_photos().add(cvPhoto);
            Structure.getUnits().get(0).getLevels().add(levelDto);
          }
        }

      }
      if (!isStructureMatched) {
        instaplanDto = intializateStructure(scan, instaplanDto, rooms);
      }
    }
    return instaplanDto;
  }

  public InstaplanDto mapRoomPhotosToStructure(String roomPhotos, InstaplanDto instaplanDto,
      Scan scan) {
    try {
      log.info("mapRoomPhotosToStructure");
      List<RoomDto> roomList = new ArrayList<>();
      if (roomPhotos != null && !roomPhotos.isBlank()) {
        JSONArray array = new JSONArray(roomPhotos);
        for (int i = 0; i < array.length(); i++) {
          RoomDto room = new RoomDto();
          room.setCv_photos(new ArrayList<>());
          CvPhoto roomCVPhoto = new CvPhoto();
          JSONObject jsonObject = array.getJSONObject(i);
          Iterator<?> keys = jsonObject.keys();
          boolean isImageUrlSplit = false;
          String imageName = "";
          while (keys.hasNext()) {
            String key = (String) keys.next();
            switch (key) {
              case "room_name" -> {
                String roomName = jsonObject.getString(key);
                if (roomName != null && !roomName.isBlank()) {
                  String roomNameVal = CVLookUpDetails.getRoomNames().get(roomName.toLowerCase());
                  if (roomNameVal != null && !roomNameVal.isBlank()) {
                    room.setRoomName(roomNameVal);
                    room.setCv_Label(roomName);
                    if (room.getRoomName().equalsIgnoreCase(CVConstants.GARAGE))
                      room.setIsGarage(true);
                  } else {
                    room.setRoomName(roomName.toUpperCase().replace(" ", "_"));
                    room.setCv_Label(roomName);
                  }
                }
              }
              case "notes" -> roomCVPhoto.setPhotoDescription(jsonObject.getString(key));
//              case "type" -> roomCVPhoto.setPhotoImgType(jsonObject.getString(key));
              case "photo_captured_time" -> {
                if (jsonObject.getString(key) != null && !jsonObject.getString(key).isBlank()) {
                  // MM-dd-yyyy-HH-mm-ss
                  String[] values = jsonObject.getString(key).split("-");
                  String dateString = values[2] + "-" + values[0] + "-" + values[1] + " "
                      + values[3] + ":" + values[4] + ":" + values[5];
                  Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString);
                  roomCVPhoto.setPhotoTimeSpan(date.getTime());
                }
              }
              case "image_url" -> {
                String url = jsonObject.getString(key);
                if (!url.isBlank() && url.contains(bucketName)) {
                  url = url.split(bucketName)[1];
                  if (url.contains(CVConstants.ZIP)) {
                    url = url.substring(0, url.lastIndexOf('/'));
                    isImageUrlSplit = true;
                  }
                }
                roomCVPhoto.setPhotoFileURL(url);
              }
              case "image_name" -> {
                imageName = jsonObject.getString(key);
              }

            }
          }

          if (isImageUrlSplit) {
            roomCVPhoto.setPhotoFileURL(roomCVPhoto.getPhotoFileURL() + imageName);
          }
          roomCVPhoto.setPhotoLatitude(scan.getLatitude());
          roomCVPhoto.setPhotoLongitude(scan.getLongitude());
          roomCVPhoto.setPhotoType("ROOM");
          room.getCv_photos().add(roomCVPhoto);
          if (scan.getScanType() != null
              && scan.getScanType().equalsIgnoreCase(CVConstants.INTERIOR_VALUE))
            room.setPosition(CVConstants.INTERIOR);
          else if (scan.getScanType() != null
              && scan.getScanType().equalsIgnoreCase(CVConstants.EXTERIOR_VALUE))
            room.setPosition(CVConstants.EXTERIOR);
          roomList.add(room);
        }
        instaplanDto = mapRoomPhotosToRoomsStructure(roomList, instaplanDto, scan);
      }
    } catch (Exception e) {
      log.error("mapRoomPhotosToStructure: Exception: " + e.getMessage());
    }
    return instaplanDto;

  }

  public InstaplanDto mapFeaturesToStructureSmartTags(String features, InstaplanDto instaplanDto,
      Scan scan) {
    try {
      log.info("mapFeaturesToStructureSmartTags");
      List<RoomDto> roomList = scanToInstaplanDtoCommonConvertor
          .mapScanPropertiestoStrucuture(features, scan, CVConstants.MID_SCAN_FEATURES);
      instaplanDto = mapScanDetailsToRoomsStructure(roomList, instaplanDto, scan, true);
    } catch (Exception e) {
      log.error("mapFeaturesToStructureSmartTags: Exception: " + e.getMessage());
    }
    return instaplanDto;
  }

  public InstaplanDto mapPlumbingFixtureToStructure(String plumbingFixture,
      InstaplanDto instaplanDto, Scan scan) {
    try {
      log.info("mapPlumbingFixtureToStructure");
      List<RoomDto> roomList = scanToInstaplanDtoCommonConvertor.mapScanPropertiestoStrucuture(
          plumbingFixture, scan, CVConstants.MID_SCAN_PLUMBING_FIXTURE);
      instaplanDto = mapScanDetailsToRoomsStructure(roomList, instaplanDto, scan, true);
    } catch (Exception e) {
      log.error("mapPlumbingFixtureToStructure: Exception: " + e.getMessage());
    }
    return instaplanDto;
  }

  public InstaplanDto mapScanUpdatesToStructure(String scanUpdates, InstaplanDto instaplanDto,
      Scan scan) {
    try {
      log.info("mapScanUpdatesToStructure");
      List<RoomDto> roomList = scanToInstaplanDtoCommonConvertor
          .mapScanPropertiestoStrucuture(scanUpdates, scan, CVConstants.MID_SCAN_UPDATES);
      instaplanDto = mapScanDetailsToRoomsStructure(roomList, instaplanDto, scan, true);
    } catch (Exception e) {
      log.error("mapScanUpdatesToStructure: Exception" + e.getMessage());
    }
    return instaplanDto;
  }

  public InstaplanDto mapInspectReportDeficienciesToProperty(String deficiencies,
      InstaplanDto instaplanDto, Map<String, String> scanRequiredFields) {
    return scanToInstaplanDtoCommonConvertor.mapInspectReportPropertiesToPropertyField(deficiencies,
        instaplanDto, scanRequiredFields, CVConstants.POST_SCAN_DEFICIENCIES);
  }



}
