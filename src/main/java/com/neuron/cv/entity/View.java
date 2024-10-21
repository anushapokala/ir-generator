package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class View {
  public String unitViewDescriptionType;
  public String unitViewQuality;
  public String unitViewDescriptionDetails;
  public List<CvPhoto> cv_photos;
}
