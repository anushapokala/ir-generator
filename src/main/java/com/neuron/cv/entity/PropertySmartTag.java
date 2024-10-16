package com.neuron.cv.entity;

import java.util.ArrayList;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertySmartTag {
  public String smartTagObjectId;
  public boolean custom;
  public String groupName;
  public String tagType;
  public String title;
  public String description;
  public String subCategory;
  public String category;
  public Date createdDate;
  @JsonProperty(value="isVisible")    
  public boolean isVisible;
  @JsonProperty(value="isOutside")    
  public boolean isOutside;
  public ArrayList<AdditionalDatum> additionalData;
  public boolean hasPhotos;
  public ArrayList<PropertyCvPhoto> cv_photos;
  
}
