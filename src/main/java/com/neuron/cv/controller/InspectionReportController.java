package com.neuron.cv.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.neuron.cv.dbentity.InspectionReport;
import com.neuron.cv.dbservice.InspectionReportService;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.entity.Root;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/inspection_report")
@AllArgsConstructor
public class InspectionReportController {

  private InspectionReportService inspectionReportService;

  @GetMapping("/{address}")
  public List<InspectionReport> getScansByAddress(@PathVariable("address") String address) {
    return inspectionReportService.getInspectionReportsByAddress(address);
  }

  @GetMapping("/generateInstaplan")
  public Root generateInstaplan(@RequestBody ParamDTO paramDTO) {
    return inspectionReportService.generateInstaplan(paramDTO);
  }

}
