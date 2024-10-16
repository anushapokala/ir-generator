package com.neuron.cv.dbrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.neuron.cv.dbentity.JobDetails;

@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails, Long> {

}
