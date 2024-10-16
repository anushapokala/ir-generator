package com.neuron.cv.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Root {
  @JsonProperty("InspectionReport")
  public InspectionReport inspectionReport;
}
