package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InteriorDeficiency {
	public String interiorDeficiencyName;
	public String interiorDeficiencyType;
    public List<String> interiorDeficiencyDetailWalls;
    public List<String> interiorDeficiencyDetailFlooring;
    public boolean interiorDeficiencySeverity;
    public String interiorDeficiencyDescription;
    public List<CvPhoto> cv_photos;
	
}
