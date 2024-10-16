package com.neuron.cv.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FieldsReplacerService {

	@Autowired
	private StorjService StorjService;

	@Value("${storj.bucketName}")
	private String bucketName;

	public String downloadFieldJsonFromStorj(String fileName) {
		byte[] fieldJson = StorjService.downloadFile(bucketName, fileName);
		String json = new String(fieldJson, StandardCharsets.UTF_8);
		return json;
	}

	public String getInspectionReportFieldPath(String model_url_3d, String house_no, String fieldName) {

		String[] url = model_url_3d.split(bucketName);
		if (house_no != null && url.length > 1) {
			String[] property = url[1].split(house_no);
			String property_path = property[0] + fieldName;
			return downloadFieldJsonFromStorj(property_path);
		}
		return null;
	}

	public String getScanFieldPath(String model_url_3d, String scan_time, String fieldName) {

		String[] url = model_url_3d.split(bucketName);
		if (scan_time != null && url.length > 1) {
			String[] property = url[1].split(scan_time);
			String property_path = property[0] + fieldName;
			return downloadFieldJsonFromStorj(property_path);
		}
		return null;
	}

	public List<String> getZipFileKeysfromMarkerFile(String markerFile) {
		List<String> zipFileKeys = new ArrayList<>();
		JSONObject jsonObject = new JSONObject(markerFile);
		JSONArray imagePathsList = (JSONArray) jsonObject.get("imagePaths");
		for (int i = 0; i < imagePathsList.length(); i++) {
			JSONObject imageObj = imagePathsList.getJSONObject(i);
			//zipfolders
			String zipPath = "";
			String zippathStr = imageObj.getString("zipPath");
			String[] zipPatharr = zippathStr.split(bucketName);
			if (zipPatharr.length > 1)
				zipPath = zipPatharr[1];
			JSONArray zipList = (JSONArray) imageObj.get("zipList");
			zipFileKeys = getFileKeysfromList(zipList,zipFileKeys,zipPath);
			
			//images
			String jpgPath = "";
			String jpgPathStr = imageObj.getString("jpgPath");
			String[] jpgPatharr = jpgPathStr.split(bucketName);
			if (jpgPatharr.length > 1)
				jpgPath = jpgPatharr[1];
			JSONArray jpgList = (JSONArray) imageObj.get("jpgList");
			zipFileKeys = getFileKeysfromList(jpgList,zipFileKeys,jpgPath);
		}
		return zipFileKeys;
	}
	
	public List<String> getFileKeysfromList(JSONArray list,List<String> zipFileKeys,String path){
		for (int i = 0; i < list.length(); i++) {
			String pathStr = path;
			pathStr = pathStr  + list.getString(i);
			zipFileKeys.add(pathStr);
		}
		return zipFileKeys;
		
	}
	
	
}
