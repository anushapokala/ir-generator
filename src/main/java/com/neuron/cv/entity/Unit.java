package com.neuron.cv.entity;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Unit {
	
  public boolean aduIndicator;
  public ArrayList<HeatingSystem> heatingSystems;
  public ArrayList<CoolingSystem> coolingSystems;
  public ArrayList<MechanicalDeficiencies> mechanicalDeficiencies;
  public ArrayList<MechanicalUpdates> mechanicalUpdates;
  public ArrayList<Garage> garages;
  public ArrayList<Level> levels;
  
}
