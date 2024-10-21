package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address {
  public String streetAddress;
  public String unitNumber;
  public String city;
  public String state;
  public String county;
  public String country;
  public String postalCode;
  public List<PropertyCvPhoto> cv_photos;
}
