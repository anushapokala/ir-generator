package com.neuron.cv.service;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.neuron.cv.dto.InspectionReportResultDto;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.entity.Root;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IRManagerService {

  @Autowired
  IRService irService;
  
  public String generateInspectionReport(ParamDTO paramDTO, boolean isSaveToStorage) {
    String result = null;
    try {
      InspectionReportResultDto resultDto = irService.generateInspectionReport(paramDTO);
      result = getFromObject(resultDto.getRoot());
    } catch (Exception e) {
      log.error("generateInspectionReport: Exception while generate report API " + e.getMessage());
    }
    return result;
  }

  public String getFromObject(Root root) throws Exception {
    log.info("getFromObject: started");
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    JavaTimeModule module = new JavaTimeModule();
    mapper.registerModule(module);

    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setSerializationInclusion(Include.NON_EMPTY);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    mapper.setDateFormat(df);
    
    return mapper.writeValueAsString(root);
  }
}
