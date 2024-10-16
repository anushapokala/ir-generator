package com.neuron.cv.entity;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address {
  public String streetAddress;
  public String streetAddress2;
  public String city;
  public String state;
  public String postalCode;
  public ArrayList<PropertyCvPhoto> cv_photos;
}
