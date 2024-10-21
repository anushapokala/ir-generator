package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SiteFeature {
	public Location location;
	public List<AdverseSiteCondition> adverseSiteConditions;
}
