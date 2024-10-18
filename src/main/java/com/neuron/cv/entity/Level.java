package com.neuron.cv.entity;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Level {
  public int levelNumber;
  public boolean belowGrade;
  public boolean attic;
  public int totalArea;
  public int finishedArea;
  public int nonStandardFinishedArea;
  public ArrayList<Room> rooms;
  public ArrayList<CvPhoto> cv_photos;
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
