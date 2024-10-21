package com.neuron.cv.entity;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Road {
	public String roadOwnershipType;
	public boolean roadMaintainedIndicator;
	public boolean yearRoundAccessIndicator;
	public ArrayList<PropertyCvPhoto> cv_photos;
}
