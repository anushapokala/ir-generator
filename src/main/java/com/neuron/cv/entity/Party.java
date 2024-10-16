package com.neuron.cv.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Party {
  public String userId;
  public String roleType;
  public String firstName;
  public String lastName;
  public String companyName;
  public String streetAddress;
  public String city;
  public String state;
  public String postalCode;
  public String phone;
  public String email;
  public String inspectionType;
  public String credentialType;
  public Object credentialId;
  public Object credentialState;

}
