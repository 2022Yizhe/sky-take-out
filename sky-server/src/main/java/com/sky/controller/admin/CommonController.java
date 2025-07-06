package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
@ApiOperation("通用接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;  // 注入阿里云 OSS 工具类对象

    /**
     * 文件上传
     * @param file multipartFile
     * @return 文件上传的 url
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestBody MultipartFile file) {

        log.info("[Controller] 文件上传开始，文件名: {}", file.getOriginalFilename());
        try {
            // 构造唯一文件名（通过 UUID），保留文件后缀
            String originalFileName = file.getOriginalFilename();
            String objectName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));

            // 上传到 OSS，返回文件的 url
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e){
            log.error("[Controller] 文件上传失败，错误信息: {}", e.getMessage());
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}
