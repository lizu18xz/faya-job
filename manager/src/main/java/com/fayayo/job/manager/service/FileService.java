package com.fayayo.job.manager.service;

import java.io.InputStream;

/**
 * @author dalizu on 2018/9/10.
 * @version v1.0
 * @desc 文件服务接口
 */
public interface FileService {

    String uploadFile(InputStream in, String path);

}
