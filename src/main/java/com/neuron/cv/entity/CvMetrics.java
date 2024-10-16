package com.neuron.cv.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CvMetrics {
  public int numberOfFloors;
  public int numberOfRooms;
  public AboveGrade aboveGrade;
  public BelowGrade belowGrade;
}
