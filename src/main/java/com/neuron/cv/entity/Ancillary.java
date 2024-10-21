package com.neuron.cv.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ancillary {
	public String roadDesc;
	public String drivewaySurfaceDesc;
	public String heatingFuelDesc;
	public Gas gas;
	public AncillaryInterior interior;
	public AncillaryExterior exterior;
	public Object alley;
	//public Alley alley;
	public AncillaryAppliances appliances;
}
