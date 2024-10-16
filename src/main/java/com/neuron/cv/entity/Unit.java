package com.neuron.cv.entity;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Unit {
  public String cv_Label;
  public String unitType;
  public ArrayList<Level> levels;
}
