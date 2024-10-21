package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MechanicalUpdate {
	public String mechanicalUpdatedComponent;
	public String mechanicalUpdateType;
	public String mechanicalUpdateTimeframe;
	public String mechanicalUpdateDescription;
	public List<CvPhoto> cv_photos;
}
