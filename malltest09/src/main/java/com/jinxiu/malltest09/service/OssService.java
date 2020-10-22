package com.jinxiu.malltest09.service;

import com.jinxiu.malltest09.dto.OssCallbackResult;
import com.jinxiu.malltest09.dto.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;

/**
 * oss上传管理Service
 */
public interface OssService {
    /**
     * oss上传策略生成
     */
    OssPolicyResult policy();

    /**
     * oss上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);
}
