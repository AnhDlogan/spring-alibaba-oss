package xyz.dassiorleando.springalibabaoss.service;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.dassiorleando.springalibabaoss.model.MetadataInfo;
import xyz.dassiorleando.springalibabaoss.util.ObjectMapperUtil;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Service
public class OssService {
    public static final Logger logger = LoggerFactory.getLogger(OssService.class);
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;
    @Value("${spring.cloud.alicloud.access-key}")
    private String accessKeyId;
    @Value("${spring.cloud.alicloud.secret-key}")
    private String accessKeySecret;
    public String genToken() {
        String roleArn = "acs:ram::5397301072429064:role/ramosstest";
        String roleSessionName = "SessionTest";
        String policy = "{\n" +
                "  \"Version\": \"1\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Action\": \"oss:*\",\n" +
                "      \"Resource\": \"*\", \n" +
                "      \"Effect\": \"Allow\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Long durationSeconds = 3600L;
        String token = "";
        try {
            String regionId = "vn";
            DefaultProfile.addEndpoint("", regionId, "sts", endpoint);
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(durationSeconds);
            AssumeRoleResponse response = client.getAcsResponse(request);
            token = response.getCredentials().getSecurityToken();
            logger.info("Expiration: " + response.getCredentials().getExpiration());
            logger.info("Access Key Id: " + response.getCredentials().getAccessKeyId());
            logger.info("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
            logger.info("Security Token: " + response.getCredentials().getSecurityToken());
            logger.info("RequestId: " + response.getRequestId());
        } catch (ClientException e) {
            logger.info("Failed:");
            logger.info("Error code: " + e.getErrCode());
            logger.info("Error message: " + e.getErrMsg());
            logger.info("RequestId: " + e.getRequestId());
        }
        return token;
    }

    public void createBucket(MetadataInfo bucket) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String bucketName = bucket.getBucketName();
        try {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setStorageClass(StorageClass.Standard);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicReadWrite);
            ossClient.createBucket(createBucketRequest);
            logger.info("Create bucket {} sucess", bucketName);
        } catch (OSSException oe) {
            logger.error("Error Message:" + oe.getErrorMessage());
            logger.error("Error Code:" + oe.getErrorCode());
            logger.error("Request ID:" + oe.getRequestId());
            logger.error("Host ID:" + oe.getHostId());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public void deleteBucket(MetadataInfo bucket) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String bucketName = bucket.getBucketName();
        try {

            ObjectListing objectListing = ossClient.listObjects(bucketName);
            List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            logger.info("delete bucket {} size {}", bucketName, objectSummaries.size());
            for (OSSObjectSummary objectSummary : objectSummaries) {
                ossClient.deleteObject(bucketName, objectSummary.getKey());
            }
            ossClient.deleteBucket(bucketName);
            logger.info("delete bucket {} sucess", bucketName);
        } catch (OSSException oe) {
            logger.error("Error Message:" + oe.getErrorMessage());
            logger.error("Error Code:" + oe.getErrorCode());
            logger.error("Request ID:" + oe.getRequestId());
            logger.error("Host ID:" + oe.getHostId());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public com.aliyun.oss.model.BucketInfo getBucketInfo(MetadataInfo bucket) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        com.aliyun.oss.model.BucketInfo bucketInfo = ossClient.getBucketInfo(bucket.getBucketName());
        logger.info("Bucket {} : {}", bucket.getBucketName(), ObjectMapperUtil.toJsonString(bucketInfo));
        ossClient.shutdown();
        return bucketInfo;
    }

    public void uploadVideo(MetadataInfo data) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(data.getBucketName(), data.getObjectName(), new File(data.getFilePath()));
            ossClient.putObject(putObjectRequest);

            //ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(pathName));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        }
        finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public void downloadVideo(MetadataInfo data) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        URL signedUrl = null;
        try {
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L);
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(data.getBucketName(), data.getObjectName(), HttpMethod.GET);
            request.setExpiration(expiration);
            signedUrl = ossClient.generatePresignedUrl(request);
            System.out.println("signed url for getObject: " + signedUrl);
        } catch (OSSException oe) {
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        }
        finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
