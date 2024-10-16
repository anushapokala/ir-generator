package com.neuron.cv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuron.cv.dbentity.JobDetails;
import com.neuron.cv.dbservice.CommonService;
import com.neuron.cv.dbservice.JobDetailsService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/job")
public class JobController {

  @Autowired
  private JobDetailsService jobservice;
  
  @Autowired
  private CommonService commonService;

  @PostMapping("/update_job_status")
  public ResponseEntity<String> updateJobStatus(@RequestHeader("hash-key") String hashKey,
      @RequestBody String jobDetails) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      String generatedKey = commonService.generateHMACkey(jobDetails);
      log.info("Generated Key: " +generatedKey);
     JobDetails jobObj = mapper.readValue(jobDetails, JobDetails.class);
      if (hashKey.equals(generatedKey))
        return new ResponseEntity<String>(jobservice.updateJobDetails(jobObj),
            HttpStatus.OK);
      else
        return new ResponseEntity<String>("Hash Key doesn't Match",
            HttpStatus.OK);

    } catch (Exception e) {
      // TODO Auto-generated catch block
      log.error("JobController : updateJobStatus:  "+e.getMessage());
      return new ResponseEntity<String>(e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

 
  
}
