package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExteriorUpdate {
	public String exteriorUpdatedComponent;
	public String exteriorUpdateType;
	public String exteriorUpdateTimeframe;
	public String exteriorUpdateDescription;
	public List<CvPhoto> cv_photos;
}
