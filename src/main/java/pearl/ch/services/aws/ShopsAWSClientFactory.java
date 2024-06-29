package pearl.ch.services.aws;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.entity.mssdb.shops.Shops;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Component
@RequiredArgsConstructor
public class ShopsAWSClientFactory implements AWSClientFactory {

	private final AWSConfig awsConfig;

	@Override
	public S3Client getClient(Shops shop) {
		AwsCredentials awsCredentials;
		switch (shop.getShop_id()) {
		case 1:
			awsCredentials = AwsBasicCredentials.create(awsConfig.getACCESS_KEY_PEARL_S3_EXPORT(),
					awsConfig.getSECRET_KEY_PEARL_S3_EXPORT());
			break;
		case 2:
			awsCredentials = AwsBasicCredentials.create(awsConfig.getACCESS_KEY_CASATIVO_S3_EXPORT(),
					awsConfig.getSECRET_KEY_CASATIVO_S3_EXPORT());
			break;
		case 3:
			awsCredentials = AwsBasicCredentials.create(awsConfig.getACCESS_KEY_NATURA_S3_EXPORT(),
					awsConfig.getSECRET_KEY_NATURA_S3_EXPORT());
			break;
		default:
			throw new IllegalArgumentException();
		}
		return buildS3Client(awsCredentials);

	}

	private S3Client buildS3Client(AwsCredentials awsCredentials) {
		return S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
				.region(Region.EU_CENTRAL_1).build();
	}

}
