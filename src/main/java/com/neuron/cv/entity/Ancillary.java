package com.neuron.cv.entity;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ancillary {
	public String roadDesc;
	public String heatingFuelDesc;
	public Interior interior;
	public Exterior exterior;
	public Object Alley;
	public Appliances appliances;
}
