package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Room {
  public String roomType;
  
  // Only for roomType=OTHER
  public String otherRoomTypeDescription;
  
  public Appliances appliances;
  public List<InteriorDeficiency> interiorDeficiencies;
  public List<InteriorUpdate> interiorUpdates;
  public List<RoomFeature> roomFeatures;
  public List<PlumbingFixture> plumbingFixtures;
  public List<CvPhoto> cv_photos;
//  public String cv_Label;
//  public String roomId;
//  public String roomName;
//  public String description;
//  public String position;
//  public Integer floorArea;
//  public Integer wallArea;
//  public Integer volume;
//  public Integer netArea;
//  @JsonProperty(value="isLivable")    
//  public Boolean isLivable;
//  @JsonProperty(value="isGarage")
//  public Boolean isGarage;
//  public Boolean affectsFloorArea;
//  @JsonProperty(value="isOutsideArea")
//  public Boolean isOutsideArea;
//  @JsonProperty(value="isFinished")
//  public Boolean isFinished;
//  @JsonProperty(value="isParking")
//  public Boolean isParking;
//  public String walkthroughUrl;
//  public String walkthroughUrlEmbedded;
//  public ArrayList<String> connections;
//  public ArrayList<SmartTag> smartTags;
//  public BreakdownStringData breakdownStringData;
//  public String ceiling;
//  public String material;
//  public String averageHeight;
//  public String minHeight;
//  public String maxHeight;
//  public Double wallFootprint;
}
