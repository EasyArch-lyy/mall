package com.jinxiu.malltestoss;

import com.jinxiu.malltestoss.common.api.CommonResult;
import com.jinxiu.malltestoss.dto.OSSUploadDto;
import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Oss {

    @Api(tags = "OssController", description = "Oss对象存储管理")
    @Controller
    @RequestMapping("/OssController")
    public class OssController {

        private final Logger LOGGER = LoggerFactory.getLogger(com.jinxiu.malltestoss.controller.OssController.class);
        @Value("${minio.endpoint}")
        private String ENDPOINT;
        @Value("${minio.bucketName}")
        private String BUCKET_NAME;
        @Value("${minio.accessKey}")
        private String ACCESS_KEY;
        @Value("${minio.secretKey}")
        private String SECRET_KEY;

        @ApiOperation("文件上传")
        @RequestMapping(value = "/upload", method = RequestMethod.POST)
        @ResponseBody
        public CommonResult upload(@RequestParam("file") MultipartFile file) {

            try {
                //创建一个MinIO的Java客户端
                MinioClient minioClient = new MinioClient(ENDPOINT, ACCESS_KEY, SECRET_KEY);
                boolean isExist = minioClient.bucketExists(BUCKET_NAME);
                if (isExist) {
                    LOGGER.info("存储桶已经存在！");
                } else {
                    //创建存储桶并设置只读权限
                    minioClient.makeBucket(BUCKET_NAME);
                    minioClient.setBucketPolicy(BUCKET_NAME, "*.*", PolicyType.READ_ONLY);
                }
                String filename = file.getOriginalFilename();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                // 设置存储对象名称
                String objectName = sdf.format(new Date()) + "/" + filename;
                // 使用putObject上传一个文件到存储桶中
                minioClient.putObject(BUCKET_NAME, objectName, file.getInputStream(), file.getContentType());
                LOGGER.info("文件上传成功!");
                OSSUploadDto OSSUploadDto = new OSSUploadDto();
                OSSUploadDto.setName(filename);
                OSSUploadDto.setUrl(ENDPOINT + "/" + BUCKET_NAME + "/" + objectName);
                return CommonResult.success(OSSUploadDto);
            } catch (Exception e) {
                LOGGER.info("上传发生错误: {}！", e.getMessage());
            }
            return CommonResult.failed();
        }

        @ApiOperation("文件删除")
        @RequestMapping(value = "/delete", method = RequestMethod.POST)
        @ResponseBody
        public CommonResult delete(@RequestParam("objectName") String objectName) {

            try {
                MinioClient minioClient = new MinioClient(ENDPOINT, ACCESS_KEY, SECRET_KEY);
                minioClient.removeObject(BUCKET_NAME, objectName);
                return CommonResult.success(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return CommonResult.failed();
        }
    }

}