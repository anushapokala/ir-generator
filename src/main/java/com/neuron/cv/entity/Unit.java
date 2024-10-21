package com.neuron.cv.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Unit {

  // Condo Only props
  public String unitConstructionStatus;
  public int floorNumber;
  public int unitMainEntrance;
  public boolean topFloor;
  public UnitView unitView;
  public List<UnitFeature> unitFeatures;
  
  //SF Only props
  public boolean aduIndicator;
  
  public List<HeatingSystem> heatingSystems;
  public List<CoolingSystem> coolingSystems;
  public List<MechanicalDeficiency> mechanicalDeficiencies;
  public List<MechanicalUpdate> mechanicalUpdates;
  public List<Garage> garages;
  public List<Level> levels;
  
}
