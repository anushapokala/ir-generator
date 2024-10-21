package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Garage {
	public String garageType;
	public int garageSpaceCount;
	public int garageSpaceArea;
	public boolean garageConversionIndicator;
	public List<GarageDeficiency> garageDeficiencies;
	public List<CvPhoto> cv_photos;
}
