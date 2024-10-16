package com.neuron.cv.entity;

import java.util.ArrayList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SiteUtility {
  public ArrayList<ElectricalService> electricalServices;
  public ArrayList<SewerService> sewerServices;
  public ArrayList<WaterService> waterServices;
  public ArrayList<FuelService> fuelServices;
}
