package com.neuron.cv.entity;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SiteUtility {
  public List<ElectricalService> electricalServices;
  public List<SewerService> sewerServices;
  public List<WaterService> waterServices;
  public List<GasService> gasServices;
}
