package com.neuron.cv.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Structure {
  public String structureType;
  public long structureArea;
  public String attachmentType;
  public List<String> foundationType;
  public String constructionStatus;
  public String constructionType;
  public boolean containsRooms;
  public int yearBuilt;
  public boolean yearBuiltEstimate;
  
  public String buildingDesign;
  public String projectName;
  public String buildingNumber;
  public int numberOfStories;
  public int numberOfElevators;
  
  public boolean condoOffstreetParkingAvailable;
  public List<CondoCarStorage> condoCarStorages;
  
  public List<ExteriorDeficiency> exteriorDeficiencies;
  public List<ExteriorUpdate> exteriorUpdates;
  public List<CvPhoto> cv_photos;
  public List<Unit> units;
//  public double cv_finishedFloorArea;
//  public double cv_nonGlaFinishedFloorArea;
//  public double cv_glaFinishedFloorArea;
//  public double cv_unfinishedFloorArea;
//  public double cv_finishedFloorAreaPercentage;
//  public double cv_grossLivingArea;
//  public double cv_grossFloorArea;
//  public double cv_garageArea;
  
}
