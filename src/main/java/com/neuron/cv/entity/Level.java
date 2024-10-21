package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Level {
  public int levelNumber;
  public boolean belowGrade;
  public boolean levelLowCeiling;
  
  public boolean attic;
  public List<String> atticAccess;
  public boolean atticAccessLocation;
  
  public int totalArea;
  public int finishedArea;
  public int nonStandardFinishedArea;
  public List<Room> rooms;
  public List<CvPhoto> cv_photos;
//public String breakdownString;
//  public String levelId;
//  public String cv_Label;
//  public String levelName;
//  public double levelOrder;
//  public Double livableArea;
//  public Double netArea;
//  public Double cv_netFloorArea;
//  public Double wallFootprintArea;
//  public Double gla;
//  public boolean hasFloorPlan;
//  public int floorplanVersion;
//  public Double cv_finishedFloorArea;
//  public Double cv_nonGlaFinishedFloorArea;
//  public Double cv_glaFinishedFloorArea;
//  public Double cv_finishedFloorAreaPercentage;
//  public Double cv_unfinishedFloorArea;
//  public Double cv_unfinishedFloorAreaPercentage;
//  public Double unfinishedArea;
//  @JsonProperty(value="isLivable")
//  public boolean isLivable;
//  public Double cv_grossFloorArea;
//  public Double cv_grossLivingArea;
//  public Double cv_garageArea;
//  public Double wallFinishedNonGlaFootprintArea;
//  public Double wallUnfinishedFootprintArea;
//  public Double wallLivableFootprintArea;
//  public Double perimeterWallFootprintArea;
//  public Double interiorWallFootprintArea;
//  @JsonProperty(value="isFinished")
//  public boolean isFinished;

}
