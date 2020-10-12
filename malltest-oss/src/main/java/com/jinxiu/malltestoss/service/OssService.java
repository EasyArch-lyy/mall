package com.jinxiu.malltestoss.service;

import com.jinxiu.malltestoss.dto.OssCallbackResult;
import com.jinxiu.malltestoss.dto.OssPolicyResult;
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
