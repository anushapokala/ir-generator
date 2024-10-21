package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnitFeature {  
  public String featureType;
  public List<CvPhoto> cv_photos;
}
