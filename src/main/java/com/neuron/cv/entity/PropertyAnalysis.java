package com.neuron.cv.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyAnalysis {
  public String propertyType;
  public String occupantType;
  public String propertyConditionRating;
  public String propertyQualityRating;
  public String locationType;
  public int yearsOwned;
}
