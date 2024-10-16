package com.neuron.cv.dto;

import java.util.ArrayList;
import com.neuron.cv.entity.CvPhoto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomDto {
  public String cv_Label;
  public String roomId;
  public String roomName;
  public String ceiling;
  public Boolean isOutsideArea;
  public ArrayList<String> connections;
  public ArrayList<SmartTagDto> smartTags;
  public String position;
  public ArrayList<CvPhoto> cv_photos;
  public Boolean isGarage;
  
}
