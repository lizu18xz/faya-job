package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.util.FtpUtil;
import com.fayayo.job.manager.config.FtpProperties;
import com.fayayo.job.manager.service.FileService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

/**
 * @author dalizu on 2018/9/10.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
     @Autowired
      private FtpProperties ftpProperties;

     /**
       *@描述 上传文件功能
      * @Return 文件名称
     */
     public String uploadFile(InputStream in,String path){
          String uploadFilename= UUID.randomUUID().toString()+ Constants.FILE_EXTENSION;
          File fileDir=new File(path);
          if(!fileDir.exists()){
               fileDir.setWritable(true);
               fileDir.mkdirs();
          }
          log.info("开始上传到临时JSON文件,路径:{},新文件名:{}",path,uploadFilename);
          File targetFile=new File(path,uploadFilename);
          try {
               //保存到临时文件
               IOUtils.copy(in,new FileOutputStream(targetFile));//保存到临时文件
               //上传文件到FTP
               FtpUtil ftpUtil=new FtpUtil(ftpProperties.getIp(),ftpProperties.getUsername(),ftpProperties.getPassword());
               ftpUtil.uploadFile(ftpProperties.getServerPath(),Lists.newArrayList(targetFile));
               //上传完成后删除upload下面的文件
               targetFile.delete();
               log.info("始上传到临时JSON文件结束......");
          } catch (IOException e) {
               e.printStackTrace();
               log.error("上传文件异常",e);
               return null;
          }
          return targetFile.getName();
     }

}
