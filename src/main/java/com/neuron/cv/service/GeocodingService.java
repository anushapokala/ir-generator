package com.neuron.cv.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse";

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
