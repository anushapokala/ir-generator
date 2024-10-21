package com.neuron.cv.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import com.neuron.cv.entity.CvGpsCoordinates;
import com.neuron.cv.entity.ElectricalService;
import com.neuron.cv.entity.Exterior;
import com.neuron.cv.entity.Features;
import com.neuron.cv.entity.FuelService;
import com.neuron.cv.entity.GasService;
import com.neuron.cv.entity.GpsCoordinates;
import com.neuron.cv.entity.Interior;
import com.neuron.cv.entity.SewerService;
import com.neuron.cv.entity.WaterService;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstaplanDto {
  
  public Instant inspectionDate;
  public Instant signatureDate;
  public Instant scannedDate;
  public Instant submittedDateAndTime;
  public Instant finishedDateAndTime;
  public Instant firstSpinCapturedAt;
  public Instant lastSpinCapturedAt;
  public GpsCoordinates gpsCoordinates;
  public CvGpsCoordinates cv_gpsCoordinates;
  public ArrayList<ElectricalService> electricalServices;
  public ArrayList<SewerService> sewerServices;
  public ArrayList<WaterService> waterServices;
  public ArrayList<GasService> gasServices;
  //public ArrayList<FuelService> fuelServices;
  public Double floorLivableArea;
  public Double floorArea;
  public Double volumen;
  public Double wallArea;
  public Double netArea;
  public Double wallFootprintArea;
  public Double gla;
  public Double finishedArea;
  public Double unfinishedArea;
  public Double totalArea;
  public Interior interior;
  public Exterior exterior;
  public ArrayList<SmartTagDto> smartTags;
  public ArrayList<StructureDto> structures;
  public Integer numberOfRooms;
  public Integer numberOfFloors;
 

}
