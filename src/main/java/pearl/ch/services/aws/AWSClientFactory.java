package pearl.ch.services.aws;

import pearl.ch.services.entity.mssdb.shops.Shops;
import software.amazon.awssdk.services.s3.S3Client;

public interface AWSClientFactory {
	
	S3Client getClient(Shops shop);
}
