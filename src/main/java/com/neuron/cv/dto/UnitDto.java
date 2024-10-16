package com.neuron.cv.dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnitDto {
  public String cv_Label;
  public String unitType;
  public ArrayList<LevelDto> levels;
}
