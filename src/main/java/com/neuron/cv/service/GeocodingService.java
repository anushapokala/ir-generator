package com.neuron.cv.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@Slf4j
public class GeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse";
    
    public static JSONObject splitAddress(String address) {
    	try {
            String urlStr = "https://nominatim.openstreetmap.org/search?q=" + 
                            address.replace(" ", "%20") + "&format=json&addressdetails=1";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");  // Required by Nominatim

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JSONArray jsonArray = new JSONArray(content.toString());
            if (jsonArray.length() > 0) {
                JSONObject json = jsonArray.getJSONObject(0);
                JSONObject addressDetails = json.getJSONObject("address");

                String city = addressDetails.optString("city", addressDetails.optString("town", ""));
                String state = addressDetails.optString("state", "");
                String zipCode = addressDetails.optString("postcode", "");
                String country = addressDetails.optString("country","");
                String county = addressDetails.optString("county","");
                System.out.println("City: " + city);
                System.out.println("State: " + state);
                System.out.println("ZIP Code: " + zipCode);
                return addressDetails;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return null;
    }
    public static String getAddress(double latitude, double longitude) throws IOException {
        String url = NOMINATIM_URL + "?lat=" + latitude + "&lon=" + longitude + "&format=json&addressdetails=1";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)") // Required for Nominatim
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                return parseAddressFromJson(json);
            }
        }
        return null;
    }

    private static String parseAddressFromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode address = root.path("address");
        JsonNode display_name = root.path("display_name");
        if(!display_name.isMissingNode()){
        	return display_name.asText();
        }
        if (!address.isMissingNode()) {
            StringBuilder formattedAddress = new StringBuilder();
            if (address.has("road")) formattedAddress.append(address.path("road").asText()).append(", ");
            if (address.has("city")) formattedAddress.append(address.path("city").asText()).append(", ");
            if (address.has("state")) formattedAddress.append(address.path("state").asText()).append(", ");
            if (address.has("country")) formattedAddress.append(address.path("country").asText());
            return formattedAddress.toString();
        }
        return null;
    }

}
