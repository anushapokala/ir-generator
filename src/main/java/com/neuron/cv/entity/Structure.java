package com.neuron.cv.entity;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Structure {
  public String structureType;
  public String structureArea;
  public String attachmentType;
  public ArrayList<String> foundationType;
  public String constructionStatus;
  public String constructionType;
  public boolean containsRooms;
  public int yearBuilt;
  public boolean yearBuiltEstimate;
  public ArrayList<ExteriorDeficiencies> exteriorDeficiencies;
  public ArrayList<ExteriorUpdates> exteriorUpdates;
  public ArrayList<CvPhoto> cv_photos;
  public ArrayList<Unit> units;
//  public double cv_finishedFloorArea;
//  public double cv_nonGlaFinishedFloorArea;
//  public double cv_glaFinishedFloorArea;
//  public double cv_unfinishedFloorArea;
//  public double cv_finishedFloorAreaPercentage;
//  public double cv_grossLivingArea;
//  public double cv_grossFloorArea;
//  public double cv_garageArea;
  
}
