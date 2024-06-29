package pearl.ch.services.aws;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import pearl.ch.services.entity.mssdb.shops.Shops;

@Getter
@Configuration
public class AWSConfig implements InitializingBean {

	@Value("${ACCESS_KEY_NATURA_S3_EXPORT}")
	private String ACCESS_KEY_NATURA_S3_EXPORT;

	@Value("${SECRET_KEY_NATURA_S3_EXPORT}")
	private String SECRET_KEY_NATURA_S3_EXPORT;

	@Value("${ACCESS_KEY_PEARL_S3_EXPORT}")
	private String ACCESS_KEY_PEARL_S3_EXPORT;

	@Value("${SECRET_KEY_PEARL_S3_EXPORT}")
	private String SECRET_KEY_PEARL_S3_EXPORT;

	@Value("${ACCESS_KEY_CASATIVO_S3_EXPORT}")
	private String ACCESS_KEY_CASATIVO_S3_EXPORT;

	@Value("${SECRET_KEY_CASATIVO_S3_EXPORT}")
	private String SECRET_KEY_CASATIVO_S3_EXPORT;

	@Value("${S3_CASATIVO_EXPORT_BUCKET}")
	public String s3CasativoExportBucket;

	@Value("${S3_CASATIVO_EXPORT_URL}")
	public String s3CasativoExportURL;

	@Value("${S3_NATURA_EXPORT_BUCKET}")
	public String s3NaturaExportBucket;

	@Value("${S3_NATURA_EXPORT_URL}")
	public String s3NaturaExportURL;

	@Value("${S3_PEARL_EXPORT_BUCKET}")
	public String s3PearlExportBucket;

	@Value("${S3_PEARL_EXPORT_URL}")
	public String s3PearlExportURL;

	private static Map<Shops, String> shopBucketMapper;
	private static Map<Shops, String> shopExportBucketMapper;

	public static String getBucketForShop(Shops shop) {
		return shopBucketMapper.get(shop);
	}

	public static String getExportBucketForShop(Shops shop) {
		return shopExportBucketMapper.get(shop);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}
}
