package com.neuron.cv.dbservice;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonService {

  @Value("${secret.key}")
  private String secretKey;
  
  public String generateHMACkey(String data) {

    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
      sha256_HMAC.init(secretKeySpec);
     return  Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    } catch (Exception e) {
      log.error("CommonService: generateHMACkey: " + e.getMessage());
    }
    return null;
  }
}
