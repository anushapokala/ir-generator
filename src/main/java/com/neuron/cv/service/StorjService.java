package com.neuron.cv.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@Slf4j
public class StorjService {

	@Autowired
	private S3Client s3Client;

	public List<String> listObjects(String bucketName, String dirPath) {
		List<String> objectKeys = new ArrayList<>();
		ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(bucketName).prefix(dirPath).build();
		ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);
		for (S3Object s3Object : listObjectsResponse.contents()) {
			objectKeys.add(s3Object.key());
		}
		return objectKeys;
	}

	public String uploadFile(File file, String bucketName, String fileKey) throws IOException {
		s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileKey).build(), file.toPath());
		file.delete(); // Clean up local file
		return "File uploaded successfully: ";
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convFile)) {
			fos.write(file.getBytes());
		}
		return convFile;
	}

	public byte[] downloadFile(String bucketName, String fileName) {
		GetObjectRequest objectRequest = GetObjectRequest.builder().bucket(bucketName).key(fileName).build();

		ResponseBytes<GetObjectResponse> responseResponseBytes = s3Client.getObjectAsBytes(objectRequest);

		return responseResponseBytes.asByteArray();
	}

	public CopyObjectResponse copyFiletoOtherFolder(String sourceBucketName, String destinationBucketName,
			String sourceKey, String destinationKey) {
		CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder().sourceBucket(sourceBucketName)
				.sourceKey(sourceKey).destinationBucket(destinationBucketName).destinationKey(destinationKey).build();
		return s3Client.copyObject(copyObjectRequest);

	}

	public List<String> copyFoldersToOtherFolder(String sourceBucketName, String destinationBucketName,
			String sourceFolder, String destinationFolder) {
		List<String> objectKeys = new ArrayList<>();
		ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(sourceBucketName)
				.prefix(sourceFolder).build();
		ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);

		for (S3Object s3Object : listObjectsResponse.contents()) {
			String sourceKey = s3Object.key();
			String destinationKey = sourceKey.replace(sourceFolder, destinationFolder);

			CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder().sourceBucket(sourceBucketName)
					.sourceKey(sourceKey).destinationBucket(destinationBucketName).destinationKey(destinationKey)
					.build();
			s3Client.copyObject(copyObjectRequest);
			objectKeys.add(s3Object.key());
		}
		return objectKeys;
	}

	public DeleteObjectResponse deleteFile(String bucketName, String key) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(key).build();

		return s3Client.deleteObject(deleteObjectRequest);
	}

	public void downloadZip(String bucketName, String objectKey, String downloadPath) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
		try {
			GetObjectResponse response = s3Client.getObject(getObjectRequest,Paths.get(downloadPath));
			
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

}
