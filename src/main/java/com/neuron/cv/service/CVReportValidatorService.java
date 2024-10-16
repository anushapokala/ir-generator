package com.neuron.cv.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CVReportValidatorService {

  @Value("${cv.report.json.schema}")
  private String jsonSchema;

  public void validateCVReport(String finalJsonpath) {
    log.info("CVReportValidatorService: validateCVReport: started");
    ObjectMapper objectMapper = new ObjectMapper();

    // create an instance of the JsonSchemaFactory using version flag
    JsonSchemaFactory schemaFactory =
        JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);

    try {
      File initialFile = new File(finalJsonpath);
      InputStream jsonStream = new FileInputStream(initialFile);

      File initialFile1 = new File(jsonSchema);
      InputStream schemaStream = new FileInputStream(initialFile1);

      // read data from the stream and store it into JsonNode
      JsonNode json = objectMapper.readTree(jsonStream);

      // get schema from the schemaStream and store it into JsonSchema
      JsonSchema schema = schemaFactory.getSchema(schemaStream);

      // create set of validation message and store result in it
      Set<ValidationMessage> validationResult = schema.validate(json);

      // show the validation errors
      if (validationResult.isEmpty()) {
        // show custom message if there is no validation error
        log.info("CVReportValidatorService: validateCVReport: There is no validation errors");

      } else {
        log.info("CVReportValidatorService: validateCVReport: Errors");
        validationResult.forEach(vm -> log.info(vm.getMessage()));
      }
    } catch (Exception ex) {
      log.error("CVReportValidatorService: validateCVReport: Exception " + ex.getMessage());
    }

  }
}
