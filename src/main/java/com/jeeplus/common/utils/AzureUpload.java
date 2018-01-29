package com.jeeplus.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.multi.MultiFileChooserUI;

import org.springframework.web.multipart.MultipartFile;

import com.jeeplus.modules.sys.utils.DictUtils;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

public class AzureUpload {
	
	
//	private static Logger logger = LoggerFactory.getLogger(AzureUpload.class);
	
//	
//	//private static AzureConfig azureConfig = BeanLocator.getBean(AzureConfig.class);
//	private static AzureConfig azureConfig = new AzureConfig();
//	// Media Services account credentials configuration
//	private MediaContract mediaService;
//
//	private String fileSuffix = null;
//
//	// Encoder configuration
//	private static final String PREFEREDENCODER = "Media Encoder Standard";
//	private static final String ENCODINIGPRESET = "H264 Single Bitrate Low Quality SD for Android";
//
//	private String pathTemp = null;

	
	public String uploadToBlob(MultipartFile file, String BlobName,String Prefix) {
		try{
			if(file==null||file.getSize()==0)return "";
			CloudStorageAccount storageAccount;
			//DictUtils.getDictValue("", type, defaultLabel)
			if(StringUtils.isNotBlank(DictUtils.getDictValue("StorageConnetion", "StorageConnetion", "")))
			{
				storageAccount = CloudStorageAccount.parse(DictUtils.getDictValue("StorageConnetion", "StorageConnetion", ""));
			}
			else storageAccount = CloudStorageAccount.parse("DefaultEndpointsProtocol=https;AccountName=bowkerstorage;AccountKey=i9cQSmIHnrqbGOGQnT1laRSxMS+TcOHYrJz0r9nevygrJH9c7RruvS/hs+JJm+WvwbjUxt/o2X6dr84OslysZQ==;EndpointSuffix=core.windows.net");

			// Create the blob client.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Get a reference to a container.
			// The container name must be lower case
			CloudBlobContainer container = blobClient.getContainerReference(BlobName);

			// Create the container if it does not exist.
			container.createIfNotExists();

			// Create a permissions object.
			BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

			// Include public access in the permissions object.
			containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

			// Set the permissions on the container.
			container.uploadPermissions(containerPermissions);

			// Define the path to a local file.
			String temp;
			if(StringUtils.isNotBlank(Prefix))
			temp = Prefix + "-" +file.getOriginalFilename();
			else temp = file.getOriginalFilename();
			CloudBlockBlob blob = container.getBlockBlobReference(temp);
			blob.getProperties().setContentType(file.getContentType());  
			byte[] 	buffer = file.getBytes();
			
			blob.uploadFromByteArray(buffer, 0, buffer.length);
			//String test = container.getStorageUri().getPrimaryUri().toString()+ "/" +file.getOriginalFilename();
			//String test = Prefix + "-" +file.getOriginalFilename();
			return temp;
		}catch (IOException | StorageException | URISyntaxException | InvalidKeyException e){
			return "";
		}
	}
	public boolean deleteBlob(String fileName,String BlobName) {
		try{
			if(StringUtils.isBlank(fileName))return false;
			CloudStorageAccount storageAccount;
			//DictUtils.getDictValue("", type, defaultLabel)
			if(StringUtils.isNotBlank(DictUtils.getDictValue("StorageConnetion", "StorageConnetion", "")))
			{
				storageAccount = CloudStorageAccount.parse(DictUtils.getDictValue("StorageConnetion", "StorageConnetion", ""));
			}
			else storageAccount = CloudStorageAccount.parse("DefaultEndpointsProtocol=https;AccountName=bowkerstorage;AccountKey=i9cQSmIHnrqbGOGQnT1laRSxMS+TcOHYrJz0r9nevygrJH9c7RruvS/hs+JJm+WvwbjUxt/o2X6dr84OslysZQ==;EndpointSuffix=core.windows.net");

			// Create the blob client.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Get a reference to a container.
			// The container name must be lower case
			CloudBlobContainer container = blobClient.getContainerReference(BlobName);

			// Create the container if it does not exist.
			container.createIfNotExists();

			// Create a permissions object.
			BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

			// Include public access in the permissions object.
			containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

			// Set the permissions on the container.
			container.uploadPermissions(containerPermissions);

			// Define the path to a local file.
			CloudBlockBlob blob = container.getBlockBlobReference(fileName);
			boolean result = false;
			result = blob.deleteIfExists();
			return result;
		}catch (StorageException | URISyntaxException | InvalidKeyException e){
			return false;
		}
	}
	
	/*
	public static void main(String[] args) {
		MediaFile mediaFile = new MediaFile();
		mediaFile.setExtension("jpg");
		mediaFile.setFilename("image_20170606213500.jpg");
		mediaFile.setFilepath("C:/Users/chenxia.he/Desktop/新建文件夹/123.png");
		mediaFile.setFileType("image");
		mediaFile.setMimeType("image/*");
		mediaFile.setRealname("image_20170606213500.jpg");
		mediaFile.setType("1");
		AzureUpload uploadAzure = new AzureUpload();
		uploadAzure.getCloudPath(mediaFile);
		System.out.println("");
	}*/
}
