package com.neuron.cv.dbservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.service.IrStorjService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarkerFileService {

	private final IrStorjService storjService;

	@Value("${storj.bucketName}")
	private String bucketName;

	
	public String getAddressFromPath(String folderPath) {
	  // Need to parse the folderPath and form addressPath
	  // instaplan-master/no-super-entity/entity-classvaluation-1/7575-frankford-rd--dallas--tx-75252--usa/935/115/
	  String[] path = folderPath.split("/");
      String addressPath = path[0] + "/" + path[1] + "/" + path[2] + "/" + path[3] + "/" + path[4] + "/" + CVConstants.JSON_FILE_UNIT_INFO;
      byte[] result = storjService.downloadFile(bucketName, addressPath);
      return new String(result);
    }
	
	public String getInspectionReportSessionFile(String folderPath, String sessionId) {
		String fileName = CVConstants.INSPECTION_REPORT_SESSION_ID_FILE + "" + sessionId + "" + CVConstants.JSON;
		String filePath = folderPath + fileName;
		// return getFilefromResource(fileName);
		byte[] result = storjService.downloadFile(bucketName, filePath);
		return new String(result);
	}

	public String getPropCalc(String folderPath, String markerFile) {
		JSONObject markerFileObj = new JSONObject(markerFile);
		String propCalcName = markerFileObj.getString(CVConstants.PROPCALC);
		String filePath = folderPath + propCalcName;
		byte[] result = storjService.downloadFile(bucketName, filePath);
		return new String(result);
	}

	// for testing local
//	public String getFilefromResource(String fileName) {
//		
//		   // Use ClassPathResource to load the file
//        ClassPathResource resource = new ClassPathResource(fileName);
//        
//        // Read the file as a String
//       
//        try {
//        	 Path path = resource.getFile().toPath();
//			return Files.readString(path);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        return null;
//		
//	}

	public void processJpgList(String markerFile) {
		JSONObject markerFileObj = new JSONObject(markerFile);
		JSONArray jpgList = (JSONArray) markerFileObj.get(CVConstants.JPGLIST);
		JSONArray zipPathList = (JSONArray) markerFileObj.get(CVConstants.ZIPPATHLIST);

		Map<String, List<String>> jpgMap = new HashedMap();
		for (int i = 0; i < zipPathList.length(); i++) {
			jpgMap.put(zipPathList.getString(i), new ArrayList<>());
		}

		for (var entry : jpgMap.entrySet()) {
			for (int jpgListIndex = 0; jpgListIndex < jpgList.length(); jpgListIndex++) {
				JSONObject jpg = jpgList.getJSONObject(jpgListIndex);
				String imagePath = jpg.getString(CVConstants.IMAGEPATH);
				if (imagePath.equalsIgnoreCase(entry.getKey())) {
					List<String> imageNames = entry.getValue();
					JSONArray imageList = (JSONArray) jpg.get(CVConstants.IMAGELIST);
					for (int imageListIndex = 0; imageListIndex < imageList.length(); imageListIndex++) {
						JSONObject imageObj = imageList.getJSONObject(imageListIndex);
						imageNames.add(imageObj.getString(CVConstants.IMAGENAME));
						log.info("path: " + entry.getKey() + "imageName: " + imageObj.getString(CVConstants.IMAGENAME));
					}
				}
			}

		}

	}

	public Map<String, String> getInspectionReportDetails(String folderPath, String sessionId) {
		Map<String, String> inspectionReportDetails = new HashedMap();
		String fileName = CVConstants.INFO + "" + sessionId + "" + CVConstants.JSON;
		String filePath = folderPath + fileName;
		byte[] result = storjService.downloadFile(bucketName, filePath);
		String resultStr = new String(result);
		JSONObject inspectionReportObj = new JSONObject(resultStr);
		Iterator<?> inspectionReportObjKeys = inspectionReportObj.keys();
		while (inspectionReportObjKeys.hasNext()) {
			String key = (String) inspectionReportObjKeys.next();
			JSONObject obj = (JSONObject) inspectionReportObj.get(key);
			inspectionReportDetails.put(key, obj.toString());
		}
		return inspectionReportDetails;

	}

}
