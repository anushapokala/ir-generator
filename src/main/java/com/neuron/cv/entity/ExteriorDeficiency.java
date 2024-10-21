package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExteriorDeficiency {
	public String exteriorDeficiencyName;
	public String exteriorDeficiencyType;
	public List<String> exteriorDeficiencyDetailWindows;
	public List<String> exteriorDeficiencyDetailExteriorWalls;
	public boolean exteriorDeficiencySeverity;
	public String exteriorDeficiencyDescription;
	public List<CvPhoto> cv_photos;
}
