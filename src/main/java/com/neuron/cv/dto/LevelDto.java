package com.neuron.cv.dto;

import java.util.ArrayList;
import com.neuron.cv.entity.CvPhoto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LevelDto {
  public String levelId;
  public String cv_Label;
  public String levelName;
  public Double cv_finishedFloorArea;
  public Double cv_nonGlaFinishedFloorArea;
  public Double cv_glaFinishedFloorArea;
  public Double cv_finishedFloorAreaPercentage;
  public Double cv_unfinishedFloorArea;
  public Double cv_unfinishedFloorAreaPercentage;
  public Double cv_grossFloorArea;
  public Double cv_grossLivingArea;
  public Double cv_garageArea;
  public Double livableArea;
  public Double gla;
  public Double finishedArea;
  public Double unfinishedArea;
  public Double netArea;
  public Double totalArea;
  public ArrayList<RoomDto> rooms;
  public ArrayList<CvPhoto> cv_photos;
  public boolean isfloorCalAPICalled=false;
  
}
