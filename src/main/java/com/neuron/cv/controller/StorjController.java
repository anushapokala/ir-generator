package com.neuron.cv.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.neuron.cv.scheduler.InspectionReportGeneratorSchedular;
import com.neuron.cv.service.StorjService;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;


@RestController
@RequestMapping("/api/v1/storj")
public class StorjController {
  
  @Autowired
 private S3Client s3Client;
  
  @Autowired
  private StorjService storjService;
  
  @Autowired
  private InspectionReportGeneratorSchedular inspectionReportGeneratorSchedular;
  
  private final String bucketName = "neuron-dev-datastore";
  private final String dirPath = "instaplan-master/instaplanpipeline/job-to-be-processed/";
  //private final String dirPath = "instaplan-master/instaplanpipeline/job-to-be-processed/";
  
  private final String superentity = "/instaplan-master/no-super-entity/";


  @GetMapping("/list")
  public List<String> listObjects() {
    List<String> objectKeys = new ArrayList<>();
    ListObjectsRequest listObjectsRequest =
        ListObjectsRequest.builder().bucket(bucketName).prefix(dirPath).build();
    ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);
    for (S3Object s3Object : listObjectsResponse.contents()) {
      objectKeys.add(s3Object.key());
    }
    return objectKeys;
  }

  @PostMapping("/upload")
  public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    File convertedFile = convertMultiPartToFile(file);
    String fileName = dirPath  + file.getOriginalFilename();
    s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName).build(),
        convertedFile.toPath());
    convertedFile.delete(); // Clean up local file
    return "File uploaded successfully: " + fileName;
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    try (FileOutputStream fos = new FileOutputStream(convFile)) {
      fos.write(file.getBytes());
    }
    return convFile;
  }
  
  @PostMapping("/download/{fileName}")
  private byte[] downloadFile(@PathVariable("fileName") String fileName) {
    return storjService.downloadFile(bucketName, fileName);
  }
  
  @PostMapping("/copy")
  public List<String> copyFoldersToAnotherFolder(@RequestParam("sourcePath") String sourcePath, @RequestParam("destPath") String destPath) {
    List<String> inputList = storjService.copyFoldersToOtherFolder(bucketName, bucketName, sourcePath, destPath);
    
    return inputList;
  }
  
  @GetMapping("/schedular")
  public void callSchedular() throws Exception {
    inspectionReportGeneratorSchedular.scheduleInspectReportGeneration();
  }
  
}
