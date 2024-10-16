package com.neuron.cv.dbrepo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.neuron.cv.dbentity.InspectionReport;

@Repository
public interface InspectionReportRepository extends JpaRepository<InspectionReport, Long> {
  Optional<InspectionReport> findByReportId(Long reportId);

  List<InspectionReport> findByAddress(String address);
}
