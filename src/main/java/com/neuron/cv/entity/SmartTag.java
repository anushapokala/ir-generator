package com.neuron.cv.entity;

import java.util.ArrayList;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmartTag {
  @JsonProperty(value="isVisible")  
  public boolean isVisible;
  public String description;
  public String surfaceType;
  public String maintainedType;
  public String tagType;
  public String subCategory;
  public String title;
  public String groupName;
  public String smartTagObjectId;
  public Date createdDate;
  public boolean hasPhotos;
  public boolean custom;
  public ArrayList<CvPhoto> cv_photos;
  public String viewQuality;
  public Object viewAffect;
  public ArrayList<Material> materials;
  public String updateType;
  public String timeframe;
  public String category;
  @JsonProperty(value="isOutside")  
  public boolean isOutside;
  public ArrayList<AdditionalDatum> additionalData;
  public String aiProvider;
  public String confidence;
  public Object brand;
  public Object serialNumber;
  public Object modelNumber;
  public Object age;
  public String windowType;
  public ArrayList<Object> features;
  public int spaceCount;
  public ArrayList<DeficiencyType> deficiencyType;
  public Object deficiencySeverity;
  public ArrayList<DeficiencyDetail> deficiencyDetail;
  public Object requiredRepair;
  public Object requiredInspection;
}
