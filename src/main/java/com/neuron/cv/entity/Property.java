package com.neuron.cv.entity;

import java.util.ArrayList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Property {
  public Address address;
  public Identification identification;
  public PropertyAnalysis propertyAnalysis;
  public Site site;
  public ArrayList<Structure> structures;
  public Double netArea;
  public Double gla;
  public Double finishedArea;
  public Double unfinishedArea;
  public Double totalArea;
  public Integer numberOfFloors;
  public Double perimeterWallFootprintArea;
  public Double interiorWallFootprintArea;
  public Integer numberOfRooms;
  public Interior interior;
  public Exterior exterior;
  public Features features;
  public CvMetrics cv_Metrics;
  public ArrayList<PropertySmartTag> smartTags;
}
