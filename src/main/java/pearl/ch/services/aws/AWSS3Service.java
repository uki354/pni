package pearl.ch.services.aws;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.entity.mssdb.shops.Shops;
import pearl.ch.services.service.shops.ShopsService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class AWSS3Service {

	private final AWSClientFactory awsClientFactory;
	
	private final ShopsService mssShopsService;

	public static final String FOLDER_STRUCTURE = "folder_structure.txt";

	public List<String> listAllFolders(String prefix, Shops shops) {
		List<String> folders = new ArrayList<>();
		String bucket = AWSConfig.getBucketForShop(shops);
		ListObjectsRequest request = ListObjectsRequest.builder().bucket(bucket).prefix(prefix).delimiter("/").build();

		ListObjectsResponse response;

		response = awsClientFactory.getClient(shops).listObjects(request);

		for (CommonPrefix commonPrefix : response.commonPrefixes()) {
			String folder = commonPrefix.prefix();
			folders.add(folder);
			folders.addAll(listAllFolders(folder, shops));
		}
		return folders;
	}

	public List<String> listAllFolders(Shops shops) {
		List<String> folders = new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(getObjectFromS3(FOLDER_STRUCTURE, shops))))) {
			String folder;
			while ((folder = bufferedReader.readLine()) != null) {
				folders.add(folder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folders;
	}

	public byte[] getObjectFromS3(String name, Shops shops) {
		byte[] response = null;
		try (S3Client client = awsClientFactory.getClient(shops)) {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(AWSConfig.getBucketForShop(shops))
					.key(name).build();
			response = client.getObject(getObjectRequest).readAllBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public void uploadFileToS3(InputStream file, String name, Shops shops) {
		String bucket = AWSConfig.getBucketForShop(shops);
		if (bucket != null) {
			try {
				PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(name).build();
				awsClientFactory.getClient(shops).putObject(putObjectRequest,
						RequestBody.fromInputStream(file, file.available()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void uploadFileToS3(InputStream file, String name, Shops shop, String contentType, String bucket) {
		if (bucket != null) {
			try {
				PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(name)
						.contentType(contentType).build();
				awsClientFactory.getClient(shop).putObject(putObjectRequest,
						RequestBody.fromInputStream(file, file.available()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void uploadImageToS3(MultipartFile image, String name, Shops shop) {
		String bucket = AWSConfig.getBucketForShop(shop);
		if (bucket != null) {
			try (S3Client client = awsClientFactory.getClient(shop)) {
				PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(name)
						.contentType(image.getContentType()).build();
				client.putObject(putObjectRequest,
						RequestBody.fromInputStream(image.getInputStream(), image.getSize()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void uploadImageToS3(File file, String name, Shops shops) {
		String bucket = AWSConfig.getBucketForShop(shops);
		if (bucket != null) {
			try (S3Client client = awsClientFactory.getClient(shops)) {
				PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(name).build();
				client.putObject(putObjectRequest, RequestBody.fromFile(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void uploadImageToS3(MultipartFile image, String name, String folder) {
		mssShopsService.getShops().forEach(shop -> {
			uploadImageToS3(image, name, shop, folder);
		});
	}

	public void uploadImageToS3(File image, String name, String folder) {
		mssShopsService.getShops().forEach((shop) -> {
			uploadImageToS3(image, name, shop, folder);
		});
	}

	public void uploadImageToS3(MultipartFile image, String name, Shops shop, String folder) {
		uploadImageToS3(image, folder + name, shop);
	}

	public void uploadImageToS3(File image, String name, Shops shop, String folder) {
		uploadImageToS3(image, folder + name, shop);
	}

}
