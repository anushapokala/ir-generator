package com.neuron.cv.service;

import org.springframework.stereotype.Service;
import com.neuron.cv.entity.Address;
import com.neuron.cv.entity.GpsCoordinates;
import com.neuron.cv.entity.Identification;
import com.neuron.cv.entity.InspectionReport;
import com.neuron.cv.entity.Level;
import com.neuron.cv.entity.Property;
import com.neuron.cv.dto.InspectionReportResultDto;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.entity.Root;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IRService {

  public InspectionReportResultDto generateInspectionReport(ParamDTO paramDTO) {
    Root root = new Root();
    InspectionReport inspectionReport = new InspectionReport();
    log.info("generateInspectionReport: started");
    
    // Get order details and add them to InspectionReport object
    // instaplan-master/no-super-entity/entity-classvaluation-1/inspection-report-orders/{userId}-{UUID}.json
    
    String folderPath = paramDTO.getFolderPath();
    String uuid = paramDTO.getSessionId();
    //markerFile.getInspectionReportOrderFile(folderPath, uuid);
    
    
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
    Address address = new Address();
    address = setupAddress(address);
    address = addAddressPhotos(address);
    property.setAddress(address);
    
    // GPS coordinates from storj
    GpsCoordinates gpsCoordinates = new GpsCoordinates();
    gpsCoordinates = setupCoordinates(gpsCoordinates);
    Identification identification = Identification.builder().gpsCoordinates(gpsCoordinates).build();
    property.setIdentification(identification);

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
  private Address setupAddress(Address _address) {
    _address.setStreetAddress(null);
    _address.setUnitNumber(null);
    _address.setCity(null);
    _address.setCounty(null);
    _address.setState(null);
    _address.setPostalCode(null);
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
