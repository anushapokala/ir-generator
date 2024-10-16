package com.neuron.cv.dto;

import com.neuron.cv.entity.Root;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InspectionReportResultDto {
  public Root root;
  private String model_url_3d;
  
  
}
