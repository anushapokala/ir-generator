package com.neuron.cv.scheduler;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.neuron.cv.dbentity.JobDetails;
import com.neuron.cv.dbservice.JobDetailsService;
import com.neuron.cv.dbservice.ScanServiceimpl;
import com.neuron.cv.dto.InspectionReportResultDto;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.service.PopulateCVJson;
import com.neuron.cv.service.StorjService;
import lombok.extern.slf4j.Slf4j;

@EnableScheduling
@Slf4j
@Service
public class InspectionReportGeneratorSchedular {

  @Value("${storj.bucketName}")
  private String bucketName;

  @Value("${storj.start.dirpath}")
  private String tobeProcessDirpath;

  @Value("${storj.intermediate.dirpath}")
  private String beingProcessedDirPath;

  @Value("${storj.end.dirpath}")
  private String completedDirpath;

  @Value("${json.creation.dirpath}")
  private String jsonCreatedPath;

  @Autowired
  private StorjService storjService;

  @Autowired
  ScanServiceimpl scanServiceimpl;

  @Autowired
  public PopulateCVJson populateCVJson;

  @Autowired
  public JobDetailsService jobService;

  @Scheduled(cron = "${jobs.cronSchedule.inspectionreport.generator}")
  public void scheduleInspectReportGeneration() {

    try {
      // list input files from storj
      log.info("scheduleInspectReportGeneration : scheduler Called at: " + LocalDateTime.now());
      List<JobDetails> jobs = new ArrayList<>();
      List<String> inputList = storjService.listObjects(bucketName, tobeProcessDirpath);

      log.info("scheduleInspectReportGeneration: List of files read: " + inputList.size());
      for (String input : inputList) {
        // download file
        byte[] inputFile = storjService.downloadFile(bucketName, input);
        // parse the input json
        ParamDTO paramdto = mapInputToParamDto(inputFile);
        // move input file to intermediate dirpath
        String[] file = input.split("/");
        String fileName = file[file.length - 1];
        String jsonCreatedFilePath = jsonCreatedPath + fileName;
        String destinationKey = beingProcessedDirPath + fileName;
        // copying to other folder
        storjService.copyFiletoOtherFolder(bucketName, bucketName, input, destinationKey);
        log.info("scheduleInspectReportGeneration: copied " + fileName + " to" + beingProcessedDirPath);
        // deleting file from to-be-process folder
        storjService.deleteFile(bucketName, input);
        log.info("scheduleInspectReportGeneration: Deleted " + fileName + " from" + tobeProcessDirpath);

        InspectionReportResultDto resultDto = scanServiceimpl.generateCVReport(paramdto);
        populateCVJson.createJsonFileFromObject(resultDto.getRoot(), jsonCreatedFilePath);
        log.info("scheduleInspectReportGeneration: Inspection Final Json File Generated and Upload to job completed Folder");
        // move final json to storj
        String finalJsonFileName = moveFinalReportFile(jsonCreatedFilePath);
        jobs.add(setJobDetails(paramdto, finalJsonFileName));
      }
      jobService.saveAll(jobs);
    } catch (Exception ex) {
      log.error("scheduleInspectReportGeneration: Exception: " + ex.getMessage());
    }

  }

  private JobDetails setJobDetails(ParamDTO paramdto, String finalJsonFileName) {
    log.info("scheduleInspectReportGeneration: Setting Job Details after final json uploaded " + finalJsonFileName);
    JobDetails job = new JobDetails();
    job.setAddress(paramdto.getAddress());
    job.setHouseNo(paramdto.getHouseNo());
    job.setUserId(Long.valueOf(paramdto.getUserId()));
    job.setFolderPath(paramdto.getFolderPath());
    job.setJobStatus("Completed");
    job.setCreatedAt(Instant.now());
    job.setModifiedAt(Instant.now());
    job.setJobName(paramdto.getJobName());
    job.setSuperEntityName(paramdto.getSuperEntity());
    job.setEntityName(paramdto.getEntity());

    return job;
  }

  private String moveFinalReportFile(String fileName) {
    File finalReport = new File(fileName);
    String[] inputName = finalReport.getName().split(".json");
    String finalFileName = "cv_" + inputName[0] + ".json";
    String destinationKey = completedDirpath + finalFileName;
    try {
      storjService.uploadFile(finalReport, bucketName, destinationKey);
      log.info("scheduleInspectReportGeneration: uploaded final report to job-completed Folder: " + finalFileName);
    } catch (IOException ex) {
      log.error(
          "moveFinalReportFile: Exception: " + ex.getMessage());
    }
    return finalFileName;

  }

  private ParamDTO mapInputToParamDto(byte[] inputFile) {
    log.info("scheduleInspectReportGeneration: mapInputToParamDto: ");
    String inputString = new String(inputFile);
    JSONObject inputObj = new JSONObject(inputString);
    ParamDTO paramdto = new ParamDTO();
    paramdto.setAddress(inputObj.getString("address"));
    paramdto.setHouseNo(inputObj.getString("house_no"));
    paramdto.setUserId(inputObj.getInt("user_id"));
    paramdto.setJobName(inputObj.getString("job_name"));
    paramdto.setSuperEntity(inputObj.getString("super_entity"));
    paramdto.setEntity(inputObj.getString("entity"));
    paramdto.setFolderPath(inputObj.getString("folder_path"));
    paramdto.setAddressPath(inputObj.getString("address_path"));
    
    log.info("scheduleInspectReportGeneration: mapInputToParamDto:  Address: " + paramdto.getAddress() + "  HouseNo: " + paramdto.getHouseNo());
    return paramdto;
  }

}
