package com.nomnom.onnomnom.global.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    public String upLoad(MultipartFile file){
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        PutObjectRequest request = PutObjectRequest.builder()
                                                    .bucket(bucketName)
                                                    .key(fileName)
                                                    .contentType(file.getContentType())
                                                    .build();
        try{
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch(IOException e){
            e.printStackTrace();
        }
        String url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
        return url;
    }

    public void deleteFile(String fileUrl){

        try{
            URI url = new URI(fileUrl);
            String key = url.getPath().substring(1);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                                                                    .bucket(bucketName)
                                                                    .key(key)
                                                                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (URISyntaxException e) {
            throw new BaseException(ErrorCode.S3_SERVICE_FAILURE, "유효하지 않은 파일 URL");
        } catch (S3Exception e) {
            throw new BaseException(ErrorCode.S3_SERVICE_FAILURE, "S3 삭제 중 오류 발생");
        }
    }
}
