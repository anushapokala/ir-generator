package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MechanicalDeficiency {
	public String mechanicalDeficiencyName;
	public String mechanicalDeficiencyType;
	public boolean mechanicalDeficiencySeverity;
	public String mechanicalDeficiencyDescription;
	public List<CvPhoto> cv_photos;
}
