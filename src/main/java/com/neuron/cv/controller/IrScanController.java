package com.neuron.cv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuron.cv.dbservice.IrCommonService;
import com.neuron.cv.dbservice.IrScanService;
import com.neuron.cv.dto.ParamDTO;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/scans")
@Slf4j
public class IrScanController {
	
	@Autowired
	private IrScanService scanService;

	@Autowired
	private IrCommonService commonService;
	
//	@GetMapping("/generateInspectionReport")
	public ResponseEntity<String> generateInspectionReport(@RequestHeader("hash-key") String hashKey,
			@RequestBody ParamDTO paramDTO) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String paramDtoString = mapper.writeValueAsString(paramDTO);
			String generatedKey = commonService.generateHMACkey(paramDtoString);
			log.info("Generated Key: " + generatedKey);
			if (hashKey.equals(generatedKey))
				return new ResponseEntity<String>(scanService.generateCVReport(paramDTO, true), HttpStatus.OK);

		} catch (Exception e) {
			log.error("IrScanController : generateInspectionReport:  " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("UnAuthorized key", HttpStatus.UNAUTHORIZED);
	}

}
