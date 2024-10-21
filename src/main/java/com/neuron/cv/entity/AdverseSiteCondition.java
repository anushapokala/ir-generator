package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdverseSiteCondition {
  public String adverseType;
  public String adverseSiteConditionDescription;
  public List<PropertyCvPhoto> cv_photos;
}
