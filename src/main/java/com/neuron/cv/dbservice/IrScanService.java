package com.neuron.cv.dbservice;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.dto.InspectionReportResultDto;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.service.CVReportValidatorService;
import com.neuron.cv.service.PopulateCVJson;
import com.neuron.cv.service.StorjService;
import lombok.extern.slf4j.Slf4j;

/**
 * Queries for instaplan.json
 **/
@Service
@Slf4j
public class IrScanService {

	  @Autowired
	  IrScanServiceImpl scanServiceimpl;

	  @Autowired
	  public PopulateCVJson populateCVJson;

	  @Value("${json.creation.dirpath}")
	  private String jsonLocalDirPath;

	  @Value("${storj.bucketName}")
	  private String bucketName;

      @Value("${cv.report.can.validate}")
      private boolean isValidateJson;

	  @Autowired
	  private StorjService storjService;

	  @Autowired
	  private CVReportValidatorService cVReportValidatorService;

	  public String generateCVReport(ParamDTO paramDTO, boolean isSaveToStorj) {
		    InspectionReportResultDto resultDto = scanServiceimpl.generateCVReport(paramDTO);
		    String result = null;
		    try {
		      result = populateCVJson.createJsonFileFromObject(resultDto.getRoot(),
		          jsonLocalDirPath + CVConstants.INSPECTION_REPORT_FILE_NAME);
		      if (isValidateJson) {
		        cVReportValidatorService
		            .validateCVReport(jsonLocalDirPath + CVConstants.INSPECTION_REPORT_FILE_NAME);
		        if (isSaveToStorj)
		          moveFinalReportFiletoStorj(paramDTO,
		              jsonLocalDirPath + CVConstants.INSPECTION_REPORT_FILE_NAME,
		              resultDto.getModel_url_3d());
		      }
		    } catch (Exception e) {
		      log.error("generateCVReport: Exception while generateCV report API " + e.getMessage());
		    }
		    return result;
		  }
	  
	  private void moveFinalReportFiletoStorj(ParamDTO paramDTO, String fileName, String model_url_3d) {
		    File finalReport = new File(fileName);
		    String[] completedDirpath = model_url_3d.split(bucketName);
		    String address_house_no_path = "";
		    String[] storjPath = null;
		    if (paramDTO.getHouseNo() != null && !paramDTO.getHouseNo().isBlank()) {
		     if(completedDirpath.length>1 && completedDirpath[1].contains(paramDTO.getHouseNo()))
		       storjPath = completedDirpath[1].split(paramDTO.getHouseNo());
		      address_house_no_path = paramDTO.getHouseNo() + CVConstants.FILE_SEPARATOR;
		    }else {
		      if(completedDirpath.length>1) {
		        storjPath = completedDirpath[1].split(CVConstants.NO_UNIT);
		       
		      }
		    }
		    String destinationKey = storjPath[0] + address_house_no_path + finalReport.getName();
		    try {
		      storjService.uploadFile(finalReport, bucketName, destinationKey);
		      log.info("moveFinalReportFiletoStorj: uploaded final report to storj Folder: "
		          + finalReport.getName() +" moved to: " + destinationKey);
		    } catch (IOException ex) {
		      log.error("moveFinalReportFile: Exception: " + ex.getMessage());
		    }

		  }
}
