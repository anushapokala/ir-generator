package com.neuron.cv.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.dbservice.IrMarkerFileService;
import com.neuron.cv.dto.InspectionReportResultDto;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.entity.Address;
import com.neuron.cv.entity.GpsCoordinates;
import com.neuron.cv.entity.Identification;
import com.neuron.cv.entity.InspectionReport;
import com.neuron.cv.entity.Level;
import com.neuron.cv.entity.Property;
import com.neuron.cv.entity.Root;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IRService {
	
	@Autowired
	private IrMarkerFileService markerFileService;
	
	@Autowired
	private GeocodingService geocodingService;

  public InspectionReportResultDto generateInspectionReport(ParamDTO paramDTO) {
    Root root = new Root();
    InspectionReport inspectionReport = new InspectionReport();
    log.info("generateInspectionReport: started");
    
    // Get order details and add them to InspectionReport object
    // instaplan-master/no-super-entity/entity-classvaluation-1/inspection-report-orders/{userId}-{UUID}.json
    
    String folderPath = paramDTO.getFolderPath();
    String uuid = paramDTO.getSessionId();
    int userId= paramDTO.getUserId();
    JSONObject inspectionReportOrderObj=markerFileService.getInspectionReportOrderFile(folderPath, uuid,userId);
    
    inspectionReport.setCaseFileID("123");
    // pdaHyperlink,collectionType,caseFileID,lpaID,pdaSubmitterEntity,propertyDataCollectorName
    // propertyDataCollectorContacts.contactMethod,propertyDataCollectorContacts.contactDetail
    // pdaCollectionEntity,propertyDataCollectorType,
    
    inspectionReport.setDataCollectorAcknowledgement(true); // get this from user submitted data
    inspectionReport.setDataCollectionDate(null); // 'YYYY-mm-dd' - get from storj 
    
    // Construct property object
    Property property = Property.builder().build();
    
    // Address details from storj
    // /neuron-dev-datastore/instaplan-master/no-super-entity/entity-classvaluation-1/address/                         
    // - address-info.json - {"longitude":"81.902824","latitude":"16.54353","address":"TEST"}
    JSONObject addressDetailsobj = markerFileService.getAddressDetails(folderPath);
    String addressStr ="";
    try {
	    addressStr = geocodingService.getAddress(Double.parseDouble(addressDetailsobj.getString(CVConstants.LATITUDE)),Double.parseDouble(addressDetailsobj.getString(CVConstants.LONGITUDE)));
//		String addressStr = geocodingService.getAddress((new Double(addressDetailsobj.getString(CVConstants.LATITUDE))), new Double(addressDetailsobj.getString(CVConstants.LONGITUDE)));
	} catch (JSONException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    JSONObject addressFromOrderObj = (JSONObject) inspectionReportOrderObj.get("Address");
    JSONObject addressDetails = (JSONObject) geocodingService.splitAddress(addressStr);    
    Address address = setupAddress(addressFromOrderObj,addressDetails);
    address = addAddressPhotos(address);
    property.setAddress(address);
    
    // GPS coordinates from storj
    GpsCoordinates gpsCoordinates = new GpsCoordinates();
    gpsCoordinates.setLatitude(Double.parseDouble(addressDetailsobj.getString(CVConstants.LATITUDE)));
    gpsCoordinates.setLongitude(Double.parseDouble(addressDetailsobj.getString(CVConstants.LONGITUDE)));
    gpsCoordinates = setupCoordinates(gpsCoordinates);
    Identification identification = Identification.builder().gpsCoordinates(gpsCoordinates).build();
    property.setIdentification(identification);
    inspectionReport.setProperty(property);
    // construct other properties from storj
    
    // Site lot props - may be ignored
    // site.lot.(lotSize, lotSizeUnits)
    
    // Structure level props - property calculations
    //"structureArea": 995,
    //"containsRooms": true,
    //"yearBuilt": 1998,
    //"yearBuiltEstimate": true,
    
    // Floor level calculations - storj -- Above-Leve1.json
    Level level = new Level();
    level.setTotalArea(0);
    level.setFinishedArea(0);
    level.setNonStandardFinishedArea(0);
    
    root.setInspectionReport(inspectionReport);
    
    InspectionReportResultDto inspectionReportDto = new InspectionReportResultDto();
    inspectionReportDto.setRoot(root);
    return inspectionReportDto;
  }
  
  // Get address details from storj - orders file, 
  private Address setupAddress(JSONObject addressFromOrderObj,JSONObject addressDetails) {
	 Address _address=new Address();
    _address.setStreetAddress(null);
    _address.setUnitNumber(null);
    if(addressFromOrderObj.get("City")!= null)
    	_address.setCity(addressFromOrderObj.getString("City"));
    if(addressDetails.get("state")!=null)
    	_address.setState(addressDetails.getString("state"));
    if(addressFromOrderObj.get("Street")!=null)
    	_address.setStreetAddress(addressFromOrderObj.getString("Street"));
    if(addressDetails.get("country") != null)
    	_address.setCountry(addressDetails.getString("country"));
    if(addressDetails.get("postcode")!=null)
    	_address.setPostalCode(addressDetails.getString("postcode"));
    if(addressDetails.get("county")!=null)
    	_address.setCounty(addressDetails.getString("county"));
    return _address;
  }
  
  // Get cv_photos from user submitted data
  private Address addAddressPhotos(Address _address) {
    return _address;
  }
  
  // Get coordinates from storj
  private GpsCoordinates setupCoordinates(GpsCoordinates _gpsCoordinates) {
    _gpsCoordinates.setLatitude(null);
    _gpsCoordinates.setLongitude(null);
    return _gpsCoordinates;
  }
  
}
