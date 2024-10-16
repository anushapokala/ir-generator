package com.neuron.cv.dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StructureDto {

  public String cv_Label;
  public String structureType;
  public String constructionType;
  public int yearBuilt;
  public boolean yearBuiltEstimate;
  public ArrayList<UnitDto> units;
}
