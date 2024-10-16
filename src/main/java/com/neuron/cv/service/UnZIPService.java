package com.neuron.cv.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

@Service
@Slf4j
public class UnZIPService {

	@Value("${zip.files.download.path}")
	private String zipFilesPath;

	@Value("${zip.files.unzipfolder.path}")
	private String destPath;

	@Value("${storj.bucketName}")
	private String bucketName;

	@Value("${zip.files.zipfolder.path}")
	private String zipFolderPath;

	@Autowired
	public StorjService storjService;

	public void getZipFiles(String storjFileKey) throws IOException {

		try {
			log.info("UnZIPService : getZipFiles : downloading file from storj");
			String[] fileNameArr = storjFileKey.split("/");
			String fileName = fileNameArr[fileNameArr.length - 1];
			String filePath = zipFilesPath;
			filePath = filePath + fileName;
			storjService.downloadZip(bucketName, storjFileKey, filePath);
			log.info("UnZIPService : getZipFiles : downloaded file from storj " + storjFileKey);
			unzip(filePath, destPath);
		} catch (Exception ex) {
			log.error("UnZIPService : getZipFiles : downloaded file from storj failed" + ex.getMessage());
		}
	}

	public void unzip(String zipFilesPath, String destPath) throws IOException {
		Path source = Paths.get(zipFilesPath);
		Path target = Paths.get(destPath);
		unzipFolder(source, target);
	}

	public void unzipFolder(Path source, Path target) throws IOException {
		try {
			log.info("UnZIPService : unzipFolder : unzippping started");
			ZipFile zipFile = new ZipFile(source.toFile());
			zipFile.extractAll(target.toString());
			log.info("UnZIPService : unzipFolder : unzippping ended");
		} catch (Exception ex) {
			log.error("UnZIPService : unzipFolder : unzippping failed" + ex.getMessage());
		}

	}

	public void zipFolder(String folderPath) {
		log.info("UnZIPService : zipFolder : zipping started");
		ZipFile zipFile = new ZipFile(zipFolderPath);
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setCompressionMethod(CompressionMethod.DEFLATE); // set compression method to deflate
		zipParameters.setCompressionLevel(CompressionLevel.NORMAL); // set compression level to normal

		try {
			// Add folder to the zip file
			zipFile.addFolder(new File(folderPath), zipParameters);
			log.info("Folder successfully zipped! " + folderPath);
			try {
				zipFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.info("UnZIPService : zipFolder : zipping closing failed " + e.getMessage());
			}
		} catch (ZipException e) {
			log.info("UnZIPService : zipFolder : zipping failed " + e.getMessage());
		}
		
	}
}
