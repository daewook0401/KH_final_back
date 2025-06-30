package com.nomnom.onnomnom.global.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final S3Service s3Service;

    public List<String> imageUpLoad(List<MultipartFile> files){
        // if (files == null || files.isEmpty()){
        //     throw new BaseException(ErrorCode.INVALID_FILE_FORMAT, "업로드할 파일이 없습니다.");
        // }
        // if (files.size() > 8){
        //     throw new BaseException(ErrorCode.FILE_SIZE_EXCEEDED, "이미지 개수가 초과 되었습니다.");
        // }
        return files.stream()
                    .map(this::upLoad)
                    .collect(Collectors.toList());
    }

    private String upLoad(MultipartFile file){
        if (file.isEmpty()){
            throw new BaseException(ErrorCode.INVALID_FILE_FORMAT, "업로드할 파일이 없습니다.");
        }
        String url = s3Service.upLoad(file);
        return url;
    }
}
