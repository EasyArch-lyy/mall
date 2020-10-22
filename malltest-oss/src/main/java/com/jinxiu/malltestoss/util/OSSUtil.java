package com.jinxiu.malltestoss.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.google.api.client.util.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSSUtil {

    private static final Logger logger = LoggerFactory.getLogger(OSSUtil.class);

    @Value("${aliyun.oss.policy.expire}")
    private static int ALIYUN_OSS_EXPIRE;
    @Value("${aliyun.oss.maxSize}")
    private static int ALIYUN_OSS_MAX_SIZE;
    @Value("${aliyun.oss.callback}")
    private static String ALIYUN_OSS_CALLBACK;
    @Value("${aliyun.oss.bucketName}")
    private static String ALIYUN_OSS_BUCKET_NAME;
    @Value("${aliyun.oss.endpoint}")
    private static String ALIYUN_OSS_ENDPOINT;
    @Value("${aliyun.oss.dir.prefix}")
    private static String ALIYUN_OSS_DIR_PREFIX;
    @Value("${aliyun.oss.accessKeyId}")
    private static String ALIYUN_OSS_ACCESSKEYID;
    @Value("${aliyun.oss.accessKeySecret}")
    private static String ALIYUN_OSS_ACCESSKEYSECRET;

    private static OSS ossClient = null;

    public static OSS creatOssClient() {
        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(ALIYUN_OSS_ENDPOINT, ALIYUN_OSS_ACCESSKEYID, ALIYUN_OSS_ACCESSKEYSECRET);
        } catch (Exception e) {
            logger.error("创建OSSClient实例失败", e.getMessage());
        }
        return ossClient;
    }

    /**
     * 文件分片上传
     */
    public static boolean ossUpload(File file, String objectName) {
        ossClient = creatOssClient();
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(ALIYUN_OSS_BUCKET_NAME, objectName);
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        String uploadId = upresult.getUploadId();
        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags = new ArrayList<>();
        // 计算文件有多少个分片。
        final long partSize = 5 * 1024 * 1024L;   // 5MB
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream instream = null;
            try {
                instream = new FileInputStream(file);
                long size = instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(ALIYUN_OSS_BUCKET_NAME);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(instream);
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
                uploadPartRequest.setPartNumber(i + 1);
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
                partETags.add(uploadPartResult.getPartETag());
                // 创建CompleteMultipartUploadRequest对象。
                // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
                CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(ALIYUN_OSS_BUCKET_NAME, objectName, uploadId, partETags);
                // 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
                // 备用completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);
                // 完成上传。
                ossClient.completeMultipartUpload(completeMultipartUploadRequest);
                // 关闭OSSClient。
                ossClient.shutdown();
                return true;
            } catch (IOException e) {
                logger.error("上传OSS服务器失败", e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 分片上传
     * <p>
     * 上传文件到OSS服务
     *
     * @param local      本地
     * @param objectName 文件路径
     * @return 0/1/2   1 是成功  0 服务器停止服务  2 服务器磁盘已满
     */
    public boolean ossUpload(String local, String objectName) {
        // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        try {
            // 判断文件是存在，如果存在则删除文件
            assert false;
            boolean found = isExit(ossClient, objectName);
            if (found) {
                ossClient.deleteObject(ALIYUN_OSS_BUCKET_NAME, objectName);
            }

            // 创建InitiateMultipartUploadRequest对象。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(ALIYUN_OSS_BUCKET_NAME, objectName);

            // 如果需要在初始化分片时设置文件存储类型，请参考以下示例代码。
            // 备用ObjectMetadata metadata = new ObjectMetadata();
            // 备用metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // 备用request.setObjectMetadata(metadata);

            // 初始化分片。
            assert false;
            InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
            // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
            String uploadId = upresult.getUploadId();

            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
            List<PartETag> partETags = new ArrayList<>();
            // 计算文件有多少个分片。
            final long partSize = 5 * 1024 * 1024L;   // 5MB
            final File sampleFile = new File(System.getProperty("catalina.home") + local);
            long fileLength = sampleFile.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }

            // 遍历分片上传。
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                InputStream instream = null;
                try {
                    instream = new FileInputStream(sampleFile);
                    // 跳过已经上传的分片。
                    instream.skip(startPos);
                    UploadPartRequest uploadPartRequest = new UploadPartRequest();
                    uploadPartRequest.setBucketName(ALIYUN_OSS_BUCKET_NAME);
                    uploadPartRequest.setKey(objectName);
                    uploadPartRequest.setUploadId(uploadId);
                    uploadPartRequest.setInputStream(instream);
                    // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
                    uploadPartRequest.setPartSize(curPartSize);
                    // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
                    uploadPartRequest.setPartNumber(i + 1);
                    // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                    UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                    // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
                    partETags.add(uploadPartResult.getPartETag());
                } catch (Exception e) {
                    logger.error("上传OSS服务器失败", e.getMessage());
                    return false;
                } finally {
                    if (instream != null) {
                        try {
                            instream.close();
                        } catch (IOException e) {
                            logger.error("上传OSS服务器失败", e.getMessage());
                        }
                    }
                }
            }

            // 创建CompleteMultipartUploadRequest对象。
            // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(ALIYUN_OSS_BUCKET_NAME, objectName, uploadId, partETags);

            // 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
            // 备用completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);

            // 完成上传。
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (Exception e) {
            logger.error("上传OSS服务器失败，请联系相关人员（卫一）", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 判断文件是否存在
     *
     * @param ossClient
     * @param objectName 文件
     * @return
     */
    public boolean isExit(OSS ossClient, String objectName) {
        // 创建OSSClient实例。
        ossClient = creatOssClient();
        boolean isFound;
        try {
            // 判断文件是否存在。doesObjectExist还有一个参数isOnlyInOSS，如果为true则忽略302重定向或镜像；如果为false，则考虑302重定向或镜像。
            isFound = ossClient.doesObjectExist(ALIYUN_OSS_BUCKET_NAME, objectName, false);
        } catch (OSSException e) {
            isFound = false;
        }
        return isFound;
    }

    public void delect(String objectName) {
        ossClient = creatOssClient();
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(ALIYUN_OSS_ENDPOINT, objectName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 列举已上传的文件
     *
     * @param KeyPrefix 指定文件的前缀
     */
    public void listParts(String KeyPrefix) {
        ossClient = creatOssClient();
        // 列举文件。 如果不设置KeyPrefix或者KeyPrefix为空，则列举存储空间下所有的文件。KeyPrefix，则列举包含指定前缀的文件。
        ObjectListing objectListing = ossClient.listObjects(ALIYUN_OSS_BUCKET_NAME, KeyPrefix);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        for (OSSObjectSummary s : sums) {
            logger.info("上传文件：", s.getKey());
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 生成下载链接
     *
     * @param objectName
     * @return
     */
    public Map<String, Object> getOSSFileUrl(String objectName) {
        Map<String, Object> data = new HashMap();
        ossClient = creatOssClient();
        // 设置URL过期时间为100年。
        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 100);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(ALIYUN_OSS_BUCKET_NAME, objectName, expiration);

        data.put("url", String.valueOf(url));
        data.put("expDate", expiration);

        // 关闭OSSClient。
        ossClient.shutdown();
        return data;
    }
}
