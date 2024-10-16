package com.neuron.cv.util;

import java.time.LocalDateTime;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.dbentity.Scan;
import com.neuron.cv.dto.InstaplanDto;
import com.neuron.cv.dto.LevelDto;
import com.neuron.cv.service.StorjService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetricsCalucatorService {

  @Autowired
  public StorjService storjService;

  @Value("${storj.bucketName}")
  private String bucketName;

  @Value("${storj.property.calc.file}")
  private String propertyCalcFileName;

  public JSONObject downloadCalCulationJsonFile(String filePath) {
    try {
      log.info("downloadCalCulationJsonFile: trying to read property-calculations.json: ");
      byte[] result = storjService.downloadFile(bucketName, filePath);
      String calc = new String(result);
      return new JSONObject(calc);
    } catch (Exception e) {
      log.info("generateCVReport :downloading property calculation json file from addresspath",
          LocalDateTime.now());
    }
    return null;

  }

  public InstaplanDto getPropertyCalcProps(JSONObject propcalData, InstaplanDto instaplanDto) {
    log.info("setPropertyLevelProps: Started");

//    String model_url_3d = scan.getModel_url_3d();
//    String[] url = model_url_3d.split(bucketName);
//    if (scan.getStructure() != null && url.length > 1) {
//      String[] property = url[1].split(scan.getStructure().toLowerCase());
//      String property_path = property[0] + propertyCalcFileName;
//      JSONObject result = downloadCalCulationJsonFile(property_path);
      return setPropertyCalcProps(instaplanDto, propcalData);
//    }
//    return instaplanDto;
  }

  // Setting property level metrics
  private InstaplanDto setPropertyCalcProps(InstaplanDto instaplanDto, JSONObject calcObj) {
    log.info("MetricsCalucatorService : setPropertyCalcProps ");
    if (calcObj == null || calcObj.isEmpty())
      return instaplanDto;
    instaplanDto.setGla(getFromJsonObject(calcObj, CVConstants.METRICS_GLA));
    instaplanDto.setNetArea(getFromJsonObject(calcObj, CVConstants.METRICS_GIA));
    instaplanDto.setFinishedArea(getFromJsonObject(calcObj, CVConstants.METRICS_FINISHED_AREA));
    instaplanDto.setUnfinishedArea(getFromJsonObject(calcObj, CVConstants.METRICS_UNFINISHED_AREA));
    instaplanDto.setTotalArea(getFromJsonObject(calcObj, CVConstants.METRICS_TOTAL_AREA));
    instaplanDto
        .setFloorLivableArea(getFromJsonObject(calcObj, CVConstants.METRICS_FLOOR_LIVABLE_AREA));
    return instaplanDto;
  }

  public LevelDto getFloorCalcProps(Scan scan, LevelDto level) {
    log.info("setPropertyLevelProps: Started");

    String model_url_3d = scan.getModel_url_3d();
    String[] url = model_url_3d.split(bucketName);
    if (scan.getGradeLevel() != null && url.length > 1) {
      String levelName = scan.getGradeLevel().replace(" ", "-").toLowerCase();
      String[] property = url[1].split(levelName.toLowerCase());
      String property_path = property[0] + levelName + "/" + scan.getGradeType() + "-"
          + scan.getGradeLevel() + ".json";
      JSONObject result = downloadCalCulationJsonFile(property_path);
      return setFloorCalcProps(level, result);
    }
    return level;
  }

  // Setting floor level metrics
  private LevelDto setFloorCalcProps(LevelDto level, JSONObject calcObj) {
    if (calcObj == null)
      return level;
    log.info("MetricsCalucatorService : setFloorCalcProps ");
    level.setGla(getFromJsonObject(calcObj, CVConstants.METRICS_GLA));
    level.setNetArea(getFromJsonObject(calcObj, CVConstants.METRICS_GIA));
    level.setCv_nonGlaFinishedFloorArea(getFromJsonObject(calcObj, CVConstants.METRICS_FINISHED_AREA));
    level.setTotalArea(getFromJsonObject(calcObj, CVConstants.METRICS_TOTAL_AREA));
    level.setCv_unfinishedFloorArea(getFromJsonObject(calcObj, CVConstants.METRICS_TOTAL_AREA));
    level.setCv_grossFloorArea(getFromJsonObject(calcObj, CVConstants.METRICS_TOTAL_AREA));
    level.setCv_grossLivingArea(getFromJsonObject(calcObj, CVConstants.METRICS_FLOOR_LIVABLE_AREA));
    level.setCv_garageArea(getFromJsonObjectIfExist(calcObj, CVConstants.METRICS_GARAGE_AREA));
    level.setIsfloorCalAPICalled(true);
    return level;
  }

  private double getFromJsonObject(JSONObject levelCalculationObj, String prop) {
    return levelCalculationObj.getDouble(prop);
  }
  private double getFromJsonObjectIfExist(JSONObject levelCalculationObj, String prop) {
    return levelCalculationObj.optDouble(prop,0);
  }


}
