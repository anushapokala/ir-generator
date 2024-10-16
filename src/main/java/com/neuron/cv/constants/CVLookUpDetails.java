package com.neuron.cv.constants;

import java.util.HashMap;
import java.util.Map;

public class CVLookUpDetails {

  public static final Map<String, String> SMARTTAG_TAGTYPE_LOOKUP = new HashMap<>();
  public static final Map<String, String> UPDATES_TAGTYPE_LOOKUP = new HashMap<>();
  public static final Map<String, String> ROOM_NAME_LOOKUP = new HashMap<>();

  public static final Map<String, String> setupSmartTagTypes() {
    SMARTTAG_TAGTYPE_LOOKUP.put("hospital", "LOCATION_HOSPITAL");
    SMARTTAG_TAGTYPE_LOOKUP.put("food service", "LOCATION_FOOD_SERVICE");
    SMARTTAG_TAGTYPE_LOOKUP.put("street left", "STREET_LEFT");
    SMARTTAG_TAGTYPE_LOOKUP.put("street right", "STREET_RIGHT");
    SMARTTAG_TAGTYPE_LOOKUP.put("view front", "VIEW_FRONT");
    SMARTTAG_TAGTYPE_LOOKUP.put("view left", "VIEW_LEFT_SIDE");
    SMARTTAG_TAGTYPE_LOOKUP.put("view right", "VIEW_RIGHT_SIDE");
    SMARTTAG_TAGTYPE_LOOKUP.put("view rear", "VIEW_REAR");
    SMARTTAG_TAGTYPE_LOOKUP.put("front center", "STRUCTURE_FRONT");
    SMARTTAG_TAGTYPE_LOOKUP.put("front left", "STRUCTURE_LEFT_SIDE");
    SMARTTAG_TAGTYPE_LOOKUP.put("front right", "STRUCTURE_RIGHT_SIDE");
    SMARTTAG_TAGTYPE_LOOKUP.put("back center", "STRUCTURE_REAR");
    SMARTTAG_TAGTYPE_LOOKUP.put("railroad", "RAILROAD");
    SMARTTAG_TAGTYPE_LOOKUP.put("park", "PARK_2");
    SMARTTAG_TAGTYPE_LOOKUP.put("trash or junk on property", "SIGNIFICANT_JUNK_TRASH");
    SMARTTAG_TAGTYPE_LOOKUP.put("sinkhole on property", "SINKHOLE");
    SMARTTAG_TAGTYPE_LOOKUP.put("childcare", "CHILDCARE");
    SMARTTAG_TAGTYPE_LOOKUP.put("eldercare", "ELDERCARE");
    SMARTTAG_TAGTYPE_LOOKUP.put("foundation", "EXTERIOR_OBSERVATION_FOUNDATION");
    SMARTTAG_TAGTYPE_LOOKUP.put("flooring", "INTERIOR_OBSERVATION_FLOORING");
    SMARTTAG_TAGTYPE_LOOKUP.put("ceiling", "INTERIOR_OBSERVATION_CEILING");
    SMARTTAG_TAGTYPE_LOOKUP.put("wood stove", "WOOD_STOVE");
    SMARTTAG_TAGTYPE_LOOKUP.put("roof", "EXTERIOR_OBSERVATION_ROOF");
    SMARTTAG_TAGTYPE_LOOKUP.put("built ins", "BUILT_INS");
    SMARTTAG_TAGTYPE_LOOKUP.put("toilet", "TOILET");
    SMARTTAG_TAGTYPE_LOOKUP.put("tub and shower combo", "TUB_AND_SHOWER_COMBO");
    return SMARTTAG_TAGTYPE_LOOKUP;
  }

  public static final Map<String, String> setupUpdateTagTypes() {
    UPDATES_TAGTYPE_LOOKUP.put("roof", "UPDATE_EXTERIOR_ROOF");
    UPDATES_TAGTYPE_LOOKUP.put("foundation", "UPDATE_EXTERIOR_FOUNDATION");
    UPDATES_TAGTYPE_LOOKUP.put("flooring", "UPDATE_INTERIOR_FLOORING");
    UPDATES_TAGTYPE_LOOKUP.put("plumbing fixture", "UPDATE_INTERIOR_PLUMBING_FIXTURES");
    return UPDATES_TAGTYPE_LOOKUP;
  }

  public static final Map<String, String> setupRoomNames() {
    ROOM_NAME_LOOKUP.put("foyer", "FOYER");
    ROOM_NAME_LOOKUP.put("hallway", "HALLWAY");
    ROOM_NAME_LOOKUP.put("kitchen", "KITCHEN");
    ROOM_NAME_LOOKUP.put("living room", "LIVING_ROOM");
    ROOM_NAME_LOOKUP.put("den", "DEN");
    ROOM_NAME_LOOKUP.put("dining", "DINING");
    ROOM_NAME_LOOKUP.put("family room", "FAMILY_ROOM");
    ROOM_NAME_LOOKUP.put("bathroom", "BATHROOM");
    ROOM_NAME_LOOKUP.put("bedroom", "BEDROOM");
    ROOM_NAME_LOOKUP.put("walk-in closet", "WALK_IN_CLOSET");
    ROOM_NAME_LOOKUP.put("stairs", "STAIRS");
    ROOM_NAME_LOOKUP.put("library", "LIBRARY");
    ROOM_NAME_LOOKUP.put("room", "ROOM");
    ROOM_NAME_LOOKUP.put("mudroom", "MUDROOM");
    ROOM_NAME_LOOKUP.put("media room", "MEDIA_ROOM");
    ROOM_NAME_LOOKUP.put("sun room", "SUN ROOM");
    ROOM_NAME_LOOKUP.put("unfinished", "UNFINISHED");
    ROOM_NAME_LOOKUP.put("utility", "UTILITY_ROOM");
    ROOM_NAME_LOOKUP.put("storage", "UTILITY_ROOM");
    ROOM_NAME_LOOKUP.put("utility/storage", "UTILITY_ROOM");
    ROOM_NAME_LOOKUP.put("shed", "SHED");
    ROOM_NAME_LOOKUP.put("stable", "STABLE");
    ROOM_NAME_LOOKUP.put("barn", "BARN");
    ROOM_NAME_LOOKUP.put("workshop", "WORKSHOP");
    ROOM_NAME_LOOKUP.put("garage", "GARAGE");
    ROOM_NAME_LOOKUP.put("other outbuilding", "OTHER_OUTBUILDING");
    return ROOM_NAME_LOOKUP;
  }

  public static final Map<String, String> getSmartTagTypes() {
    return SMARTTAG_TAGTYPE_LOOKUP;
  }

  public static final Map<String, String> getUpdateTagTypes() {
    return UPDATES_TAGTYPE_LOOKUP;
  }

  public static final Map<String, String> getRoomNames() {
    return ROOM_NAME_LOOKUP;
  }

}
