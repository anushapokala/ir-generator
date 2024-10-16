package com.neuron.cv.dbservice;

import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuron.cv.dbentity.JobDetails;
import com.neuron.cv.dbrepo.JobDetailsRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Queries for instaplan.json
 **/
@Service
@Slf4j
public class JobDetailsService {

  @Autowired
  JobDetailsRepository jobRepo;


  public void saveAll(List<JobDetails> jobs) {
    jobRepo.saveAll(jobs);
  }

  public String updateJobDetails(JobDetails job) {
    JobDetails jobDetails = jobRepo.findById(job.getJobId()).get();
    if (jobDetails != null) {
      jobDetails.setJobStatus(job.getJobStatus());
      jobRepo.save(jobDetails);
      return "Updated SuccessFully!";
    }
    return "No Record Found with Job Id";
  }

 

}
