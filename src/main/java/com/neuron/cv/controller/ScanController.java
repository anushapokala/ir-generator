package com.neuron.cv.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuron.cv.dbentity.Scan;
import com.neuron.cv.dbservice.CommonService;
import com.neuron.cv.dbservice.ScanService;
import com.neuron.cv.dto.ParamDTO;
import com.neuron.cv.service.FieldsReplacerService;
import com.neuron.cv.service.StorjService;
import com.neuron.cv.service.UnZIPService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/scans")
@Slf4j
public class ScanController {

	@Autowired
	private ScanService scanService;
	@Autowired
	private UnZIPService unZipService;

	@Value("${zip.files.unzipfolder.path}")
	private String destPath;
	
	@Value("${zip.files.zipfolder.path}")
	private String zipFolderPath;
	
	@Value("${storj.bucketName}")
	private String bucketName;

	@Autowired
	private StorjService StorjService;
	
	@Autowired
	private FieldsReplacerService fieldsReplacerService;
	

	@Autowired
	private CommonService commonService;

	@GetMapping("/{address}")
	public List<Scan> getScansByAddress(@PathVariable("address") String address) {
		return scanService.getScansByAddress(address);
	}

	@GetMapping("/generateCVReport")
	public String generateCVReport(@RequestBody ParamDTO paramDTO) {
		return scanService.generateCVReport(paramDTO, false);
	}

	/*
	 * {
          "userName":"varsha@neuron3d.com",
          "jobName":"job1",
          "userId":"115",
          "entity":"class-valuation-1",
          "folderPath":"instaplan-master/no-super-entity/entity-classvaluation-1/7575-frankford-rd--dallas--tx-75252--usa/935/115/",
          "schema":"http://json-schema.org/draft-07/schema#",
          "priorSessionId":"",
          "sessionId":"A18DD1B2-528D-47FD-86F5-E6E897D6A426",
          "jobDescription":"Defines the structure  for a POST request to the Inspection Report workflow automation, initially designed for Class Valuation",
          "type":"object",
          "jobVersion":"1.0",
          "submitTime":"2024-09-16T23:47:54.2+0000",
          "scheduledTime":"2024-09-16T23:47:54.2+0000",
          "title":"Payload for Inspection Report Pipeline"
       }
       folderPath, sessionId, priorSessionId
       
	 * */
	
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
			// TODO Auto-generated catch block
			log.error("ScanController : generateCVReport:  " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("UnAuthorized key", HttpStatus.UNAUTHORIZED);

	}

	//for testing purpose
	@GetMapping("/downloadZip")
	public void downloadZip(@RequestParam String fileName) throws IOException {
		unZipService.getZipFiles(fileName);
	}

	//for testing purpose
	@GetMapping("/zip")
	public void zipFolder() throws IOException {
		unZipService.zipFolder(destPath);
	}
	
	//for testing purpose
	@GetMapping("/uploadZip")
	public void uplaodZip(@RequestParam String fileKey) throws IOException {
		File file = new File(zipFolderPath);
		StorjService.uploadFile(file, bucketName, fileKey);
	}
	
	//for testing purpose
		@GetMapping("/zipPathList")
		public List<String> getZIpPathList(@RequestBody String markerFile) throws IOException {
			return fieldsReplacerService.getZipFileKeysfromMarkerFile(markerFile);
		}

}
