package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Locations {
	public String locationDescriptionType;
	public String locationDescriptionDetails;
	public List<CvPhoto> cv_photos;
}
