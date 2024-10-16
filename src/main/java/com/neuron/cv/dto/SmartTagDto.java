package com.neuron.cv.dto;

import java.util.ArrayList;
import java.util.Date;
import com.neuron.cv.entity.PropertyCvPhoto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmartTagDto {
  public boolean isVisible;
  public String description;
  public String tagType;
  public String subCategory;
  public String title;
  public String groupName;
  public String smartTagObjectId;
  public Date createdDate;
  public boolean hasPhotos;
  public boolean custom;
  public ArrayList<PropertyCvPhoto> cv_photos;
 
}
