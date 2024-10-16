package com.neuron.cv.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.constants.CVLookUpDetails;
import com.neuron.cv.dbentity.Scan;
import com.neuron.cv.dto.InstaplanDto;
import com.neuron.cv.dto.RoomDto;
import com.neuron.cv.dto.SmartTagDto;
import com.neuron.cv.entity.PropertyCvPhoto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScanToInstaplanDtoCommonConvertor {
  @Value("${storj.bucketName}")
  private String bucketName;

  public List<RoomDto> mapScanPropertiestoStrucuture(String drfu, Scan scan,String scanType) {
    List<RoomDto> roomList = new ArrayList<>();
    try {
      if (drfu != null && !drfu.isBlank()) {
        JSONArray array = new JSONArray(drfu);
        for (int i = 0; i < array.length(); i++) {
          RoomDto room = new RoomDto();
          room.setSmartTags(new ArrayList<>());
          SmartTagDto smartTag = new SmartTagDto();
          boolean isImageUrlSplit = false;
          String imageName = "";
          smartTag.setGroupName(scanType);
          PropertyCvPhoto propertyCVPhoto = new PropertyCvPhoto();
          JSONObject drfuObject = array.getJSONObject(i);
          Iterator<?> keys = drfuObject.keys();
          while (keys.hasNext()) {
            String key = (String) keys.next();
            switch (key) {
              case "id" -> smartTag.setSmartTagObjectId(drfuObject.getString(key));
              case "deficiency_type" -> {
                smartTag.setTitle(drfuObject.getString(key));
                smartTag.setSubCategory(drfuObject.getString(key));

                if (drfuObject.getString(key) != null && !drfuObject.getString(key).isBlank()) {
                  smartTag.setTagType(CVLookUpDetails.getSmartTagTypes()
                      .get(drfuObject.getString(key).toLowerCase()));
                }
              }
              case "property_use_type" -> {
                smartTag.setTitle(drfuObject.getString(key));
                smartTag.setSubCategory(drfuObject.getString(key));
                if (drfuObject.getString(key) != null && !drfuObject.getString(key).isBlank()) {
                  smartTag.setTagType(CVLookUpDetails.getSmartTagTypes()
                      .get(drfuObject.getString(key).toLowerCase()));
                }
              }
              case "update_type" -> {
                smartTag.setTitle(drfuObject.getString(key));
                smartTag.setSubCategory(drfuObject.getString(key));
                if (drfuObject.getString(key) != null && !drfuObject.getString(key).isBlank()) {
                  smartTag.setTagType(CVLookUpDetails.getUpdateTagTypes()
                      .get(drfuObject.getString(key).toLowerCase()));
                }
              }
             
              case "plumbing_fixture_type" -> {
                smartTag.setTitle(drfuObject.getString(key));
                smartTag.setSubCategory(drfuObject.getString(key));
                if (drfuObject.getString(key) != null && !drfuObject.getString(key).isBlank()) {
                  smartTag.setTagType(CVLookUpDetails.getSmartTagTypes()
                      .get(drfuObject.getString(key).toLowerCase()));
                }
              }
              case "feature_type" -> {
                smartTag.setTitle(drfuObject.getString(key));
                smartTag.setSubCategory(drfuObject.getString(key));

                if (drfuObject.getString(key) != null && !drfuObject.getString(key).isBlank()) {
                  smartTag.setTagType(CVLookUpDetails.getSmartTagTypes()
                      .get(drfuObject.getString(key).toLowerCase()));
                }
              }
              case "photo_captured_time" -> {
                if (drfuObject.getString(key) != null && !drfuObject.getString(key).isBlank()) {
                  // MM-dd-yyyy-HH-mm-ss
                  String[] values = drfuObject.getString(key).split("-");
                  String dateString = values[2] + "-" + values[0] + "-" + values[1] + " "
                      + values[3] + ":" + values[4] + ":" + values[5];
                  Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString);
                  propertyCVPhoto.setPhotoTimeSpan(date.getTime());
                }
              }
              case "image_url" -> {
                String url = drfuObject.getString(key);
                if (!url.isBlank() && url.contains(bucketName)) {
                  url = url.split(bucketName)[1];
                  if (url.contains(CVConstants.ZIP)) {
                    url = url.substring(0, url.lastIndexOf('/'));
                    isImageUrlSplit = true;
                  }
                }
                propertyCVPhoto.setPhotoFileURL(url);
              }
              case "image_name" -> {
                imageName = drfuObject.getString(key);
              }

              case "room_name" -> {
                String roomName = drfuObject.getString(key);
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
              
              case "notes" -> smartTag.setDescription(drfuObject.getString(key));
            //  case "is_uploaded" -> log.info("is_uploaded: ", drfuObject.get(key));

            };
          } ;
          if (CollectionUtils.isEmpty(smartTag.getCv_photos())) {
            smartTag.setCv_photos(new ArrayList<>());
          }
          if (isImageUrlSplit) {
            propertyCVPhoto.setPhotoFileURL(propertyCVPhoto.getPhotoFileURL() + imageName);
          }
          propertyCVPhoto.setPhotoLatitude(scan.getLatitude());
          propertyCVPhoto.setPhotoLongitude(scan.getLongitude());
          propertyCVPhoto.setPhotoType("ROOM");
          smartTag.getCv_photos().add(propertyCVPhoto);
          smartTag.setHasPhotos(true);
          if (scan.getScanType() != null
              && scan.getScanType().equalsIgnoreCase(CVConstants.INTERIOR_VALUE))
            room.setPosition(CVConstants.INTERIOR);
          else if (scan.getScanType() != null
              && scan.getScanType().equalsIgnoreCase(CVConstants.EXTERIOR_VALUE))
            room.setPosition(CVConstants.EXTERIOR);
          room.getSmartTags().add(smartTag);
          roomList.add(room);
        }
      }

    } catch (Exception e) {
      log.error("mapScanPropertiestoStrucuture: Exception: " + e.getMessage());
    }
    return roomList;
  }

  public InstaplanDto mapInspectReportPropertiesToPropertyField(String deficiencies,
      InstaplanDto instaplanDto, Map<String, String> scanRequiredFields,String inspection_property_type) {
    try {
      log.info("mapInspectReportPropertiesToPropertyField");
     
      if (deficiencies != null && !deficiencies.isBlank()) {
        JSONObject deficienciesObj = new JSONObject(deficiencies);
        Iterator<?> deficienciesKeys = deficienciesObj.keys();
        while (deficienciesKeys.hasNext()) {
        List<SmartTagDto> propertySmartTagList = new ArrayList<>();
          String pdapiKey = (String) deficienciesKeys.next();
          JSONArray array = (JSONArray) deficienciesObj.get(pdapiKey);
          for (int i = 0; i < array.length(); i++) {
            SmartTagDto smartTag = new SmartTagDto();
            smartTag.setGroupName(inspection_property_type);

            PropertyCvPhoto propertyCVPhoto = new PropertyCvPhoto();
            JSONObject jsonObject = array.getJSONObject(i);
            Iterator<?> keys = jsonObject.keys();
            while (keys.hasNext()) {
              String key = (String) keys.next();
              // isVisible,tagType,subCategory,title,groupName,smartTagObjectId,createdDate,hasPhotos,custom,description
              switch (key) {
                case "id" -> smartTag.setSmartTagObjectId(jsonObject.getString(key));
                case "deficiency_type" -> {
                  smartTag.setTitle(jsonObject.getString(key));
                  smartTag.setSubCategory(jsonObject.getString(key));
                  if (jsonObject.getString(key) != null && !jsonObject.getString(key).isBlank()) {
                    smartTag.setTagType(CVLookUpDetails.getSmartTagTypes()
                        .get(jsonObject.getString(key).toLowerCase()));
                  }
                }
                case "location_type" -> {
                  smartTag.setTitle(jsonObject.getString(key));
                  smartTag.setSubCategory(jsonObject.getString(key));
                  if (jsonObject.getString(key) != null && !jsonObject.getString(key).isBlank()) {
                    smartTag.setTagType(CVLookUpDetails.getSmartTagTypes()
                        .get(jsonObject.getString(key).toLowerCase()));
                  }
                }
                case "pdapi_type" -> {
                  smartTag.setTitle(jsonObject.getString(key));
                  smartTag.setSubCategory(jsonObject.getString(key));
                  if (jsonObject.getString(key) != null && !jsonObject.getString(key).isBlank()) {
                    smartTag.setTagType(CVLookUpDetails.getSmartTagTypes()
                        .get(jsonObject.getString(key).toLowerCase()));
                  }
                }
                case "view_type" -> {
                  smartTag.setTitle(jsonObject.getString(key));
                  smartTag.setSubCategory(jsonObject.getString(key));
                  if (jsonObject.getString(key) != null && !jsonObject.getString(key).isBlank()) {
                    smartTag.setTagType(CVLookUpDetails.getSmartTagTypes()
                        .get(jsonObject.getString(key).toLowerCase()));
                  }
                }
                case "update_type" -> {
                  smartTag.setTitle(jsonObject.getString(key));
                  smartTag.setSubCategory(jsonObject.getString(key));
                  if (jsonObject.getString(key) != null && !jsonObject.getString(key).isBlank()) {
                    smartTag.setTagType(CVLookUpDetails.getUpdateTagTypes()
                        .get(jsonObject.getString(key).toLowerCase()));
                  }
                }
                case "notes" -> smartTag.setDescription(jsonObject.getString(key));
                case "isYes" -> {
                  if(inspection_property_type.equalsIgnoreCase(CVConstants.POST_SCAN_PDAPI))
                    smartTag.setVisible(jsonObject.getBoolean(key));
                  else if (jsonObject.getString(key).equalsIgnoreCase("NO"))
                    smartTag.setVisible(false);
                  else if (jsonObject.getString(key).equalsIgnoreCase("Yes"))
                    smartTag.setVisible(true);
                
                }
                case "photo_captured_time" -> {
                  if (jsonObject.getString(key) != null && !jsonObject.getString(key).isBlank()) {
                    // MM-dd-yyyy-HH-mm-ss
                    String[] values = jsonObject.getString(key).split("-");
                    String dateString = values[2] + "-" + values[0] + "-" + values[1] + " "
                        + values[3] + ":" + values[4] + ":" + values[5];
                    Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString);
                    propertyCVPhoto.setPhotoTimeSpan(date.getTime());
                  }
                }

                case "image_url" -> {
                  String url = jsonObject.getString(key);
                  if (!url.isBlank() && url.contains(bucketName))
                    url = url.split(bucketName)[1];
                  propertyCVPhoto.setPhotoFileURL(url);
                }
              }
            }
            if (smartTag.isVisible()) {
              if (CollectionUtils.isEmpty(smartTag.getCv_photos())) {
                smartTag.setCv_photos(new ArrayList<>());
              }
              if (!CollectionUtils.isEmpty(scanRequiredFields)) {
                propertyCVPhoto.setPhotoLatitude(scanRequiredFields.get("latitude"));
                propertyCVPhoto.setPhotoLongitude(scanRequiredFields.get("longitude"));
              }
              propertyCVPhoto.setPhotoType("OTHER");
              smartTag.getCv_photos().add(propertyCVPhoto);
              smartTag.setHasPhotos(true);
              propertySmartTagList.add(smartTag);
            }
          }
          if (CollectionUtils.isEmpty(instaplanDto.getSmartTags())) {
            instaplanDto.setSmartTags(new ArrayList<>());
          }
          instaplanDto.getSmartTags().addAll(propertySmartTagList);
        }
      }

    } catch (Exception e) {
      log.error("mapInspectReportPropertiesToPropertyField: Exception: " + e.getMessage());
    }
    return instaplanDto;
  }
}
