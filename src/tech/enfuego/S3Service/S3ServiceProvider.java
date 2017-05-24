package tech.enfuego.S3Service;


import java.io.InputStream;
import java.net.URL;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionImpl;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.AWSS3V4Signer;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3ServiceProvider {


	private static AWSCredentials awscredentials;

	private static AmazonS3 s3;

	private final static String BUCKET = "elasticbeanstalk-us-west-2-422535611875";

	private static AWSCredentials getAWSCredentials() {

		if (awscredentials == null) {
			awscredentials = new BasicAWSCredentials("AKIAJ7YAJ7PHFRO43KYA", "VLzZHeX+Gq5hDYHgCEkjtGsA7q3/IkCpjbxtlzg3");

		}
		return awscredentials;
	}

	public static URL uploadFiletoS3(InputStream is, ObjectMetadata objmetadata, String email) {
		String key = "profile-photos/"+email;
		try
		{
			if(s3 == null) {
				s3 = new AmazonS3Client(getAWSCredentials());
				s3.setRegion(Region.getRegion(Regions.US_WEST_2));
			}
			
			PutObjectRequest pr = new PutObjectRequest(BUCKET, key, is, objmetadata);
			pr.withCannedAcl(CannedAccessControlList.PublicRead);

			s3.putObject(pr);
		}
		catch(Exception e) {
			throw e;
		}
		URL url = s3.getUrl(BUCKET, key);
		return url;
	}


}
