package com.neuron.cv.entity;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Structure {
  public String cv_Label;
  public String structureType;
  public ArrayList<Unit> units;
  public String constructionType;
  public int yearBuilt;
  public boolean yearBuiltEstimate;
//  public double cv_finishedFloorArea;
//  public double cv_nonGlaFinishedFloorArea;
//  public double cv_glaFinishedFloorArea;
//  public double cv_unfinishedFloorArea;
//  public double cv_finishedFloorAreaPercentage;
//  public double cv_grossLivingArea;
//  public double cv_grossFloorArea;
//  public double cv_garageArea;
  
}
