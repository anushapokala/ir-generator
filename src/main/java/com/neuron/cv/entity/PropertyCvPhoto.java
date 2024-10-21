package com.neuron.cv.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyCvPhoto {
  
  public String photoType;
  public String photoLatitude;
  public String photoLongitude;
  public String photoDescription;
  public Long photoTimeSpan;
  public String photoFileURL;
  
  //public String photoImgType;
  public String photoId;
  public String aiProvider;
  public int rank;
  public int confidence;
  public boolean _seleccted;
}
