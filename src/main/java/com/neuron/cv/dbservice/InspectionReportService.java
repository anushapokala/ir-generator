package com.neuron.cv.dbservice;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neuron.cv.dbentity.InspectionReport;
import com.neuron.cv.dbrepo.InspectionReportRepoImpl;
import com.neuron.cv.dbrepo.InspectionReportRepository;
import com.neuron.cv.dto.InstaplanDto;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.entity.ElectricalService;
import com.neuron.cv.entity.FuelService;
import com.neuron.cv.entity.Root;
import com.neuron.cv.entity.SewerService;
import com.neuron.cv.entity.WaterService;
import com.neuron.cv.service.PopulateCVJson;
import com.neuron.cv.util.ScanToInstaplanDtoConvertor;

@Service
public class InspectionReportService {

  @Autowired
  InspectionReportRepository inspectionReportRepository;
  
  @Autowired
  private InspectionReportRepoImpl inspectionReportRepoImpl;

  @Autowired
  public ScanToInstaplanDtoConvertor scanToInstaplanDtoConvertor;
  
  @Autowired
  public PopulateCVJson populateCVJson;

  public List<InspectionReport> getInspectionReportsByAddress(String address) {
    List<InspectionReport> scans = inspectionReportRepository.findByAddress(address);
    return scans;
  }

  public InspectionReport getInspectionReportsByReportId(Long reportId) {
    return inspectionReportRepository.findByReportId(reportId).get();
  }

  public Root generateInstaplan(ParamDTO paramDTO)  {
    Root root = new Root();
    List<InspectionReport> reports = inspectionReportRepoImpl.getInspectionReports(paramDTO);
    InstaplanDto instaplanDto = new InstaplanDto();
   initializeInstaplanDto(instaplanDto);
    for (InspectionReport report : reports) {
      scanToInstaplanDtoConvertor.mapSiteUtilities(report.getUtilities(),instaplanDto);
    }
   try {
   root = populateCVJson.generateJson(null, instaplanDto);
  } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
    return root;
  }

  private void initializeInstaplanDto(InstaplanDto instaplanDto) {
    ArrayList<ElectricalService> electricalServices = new ArrayList<ElectricalService>();
    ArrayList<SewerService> sewerServices = new ArrayList<>();
    ArrayList<WaterService> waterServices = new ArrayList<>();
    ArrayList<FuelService> fuelServices = new ArrayList<>();
    instaplanDto.setElectricalServices(electricalServices);
    instaplanDto.setSewerServices(sewerServices);
    instaplanDto.setWaterServices(waterServices);
    instaplanDto.setFuelServices(fuelServices);
    
  }

}
