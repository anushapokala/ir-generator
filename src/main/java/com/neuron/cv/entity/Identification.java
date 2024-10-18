package com.neuron.cv.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Identification {
  public GpsCoordinates gpsCoordinates;
 // public CvGpsCoordinates cv_gpsCoordinates;
}
