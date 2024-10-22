package com.neuron.cv.entity;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Property {
public String propertyType;
  public boolean propertyOccupied;
  public Address address;
  public Identification identification;
  public Site site;
  public List<Structure> buildings;
  //public PropertyAnalysis propertyAnalysis;
  public List<PropertyCvPhoto> cv_photos;
  public Ancillary ancillary;
}
