package com.neuron.cv.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Site {
  public Lot lot;
  public SiteUtility siteUtility;
}