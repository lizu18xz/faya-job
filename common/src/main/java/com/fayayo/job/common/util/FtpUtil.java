package com.fayayo.job.common.util;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.List;

@Slf4j
@Data
public class FtpUtil {

    private String ip;
    private Integer port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FtpUtil(String ip, String user, String pwd) {
        this.ip = ip;
        this.user = user;
        this.pwd = pwd;
        this.port=21;
    }

    public boolean uploadFile(String remotePath,List<File>fileList) throws IOException {
        log.info("上传路径:"+remotePath);
        boolean uploaded=true;
        FileInputStream fis=null;
        //连接ftp
        if(connectServer(this.getIp(),this.getPort(),this.getUser(),this.getPwd())){
            try {
                boolean existDir=ftpClient.changeWorkingDirectory(remotePath);
                if(!existDir){
                    log.info("上传路径不存在");
                    //创建上传的路径
                    boolean isCreate=createMultiDirectory(remotePath);
                    if(!isCreate){
                        log.info("创建目录失败");
                        return false;
                    }
                    log.info("目录创建成功");
                }
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();;

                for (File fileItem:fileList){
                    fis=new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
                log.info("结束上传");
            } catch (Exception e) {
                log.error("上传文件异常");
                uploaded=false;
                e.printStackTrace();
            }finally {
                if(fis!=null){
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }else {
            log.error("ftp连接失败");
            throw new CommonException(ResultEnum.FTP_LOGIN_FAIL);
        }
        return uploaded;
    }

    private boolean connectServer(String ip, Integer port, String user, String pwd){
        boolean isSuccess=false;
        ftpClient=new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess=  ftpClient.login(user,pwd);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("连接ftp失败");
        }
        return isSuccess;
    }

    //创建多级目录
    private boolean createMultiDirectory(String multiDirectory) {
        boolean bool = false;
        try {
            String[] dirs = multiDirectory.split("/");
            ftpClient.changeWorkingDirectory("/");
            //按顺序检查目录是否存在，不存在则创建目录
            for(int i=1; dirs!=null&&i<dirs.length; i++) {
                if(!ftpClient.changeWorkingDirectory(dirs[i])) {
                    if(ftpClient.makeDirectory(dirs[i])) {
                        if(!ftpClient.changeWorkingDirectory(dirs[i])) {
                            return false;
                        }
                    }else {
                        return false;
                    }
                }
            }
            bool = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return bool;
        }
    }

     /**
       *@描述 删除ftp上的文件
     */
     public void deleteFile(String filename,String ftpPath){

         log.info("删除文件:"+filename);
         //获取连接
         FTPClient ftpClient=getFtpClient();

         try {
             ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
             ftpClient.enterLocalPassiveMode();
             ftpClient.changeWorkingDirectory(ftpPath);

             boolean flag = ftpClient.deleteFile(filename);
             if(!flag){
             }
         } catch (Exception e) {
             e.printStackTrace();
             log.info("删除文件:"+filename+"失败，请管理员手动删除");
         }finally {
             try {
                 ftpClient.disconnect();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }

      /**
        *@描述 下载ftp文件到指定目录 远程地址 文件名称 目标路径
      */
    public void downLoadFtpFile(String ftpPath,String filename,String distPath){
        //获取连接
        if(connectServer(this.getIp(),this.getPort(),this.getUser(),this.getPwd())){

            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            try {
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.changeWorkingDirectory(ftpPath);
                //InputStream in=ftpClient.retrieveFileStream(new String(filename.getBytes("utf-8"),"iso-8859-1"));
                InputStream in=ftpClient.retrieveFileStream(filename);
                FileOutputStream outputStream=new FileOutputStream(new File(distPath));
                IOUtils.copy(in,outputStream);

            } catch (Exception e) {
                e.printStackTrace();
                throw new CommonException(ResultEnum.FTP_DOWN_FAIL);
            }finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            log.error("ftp连接失败");
            throw new CommonException(ResultEnum.FTP_LOGIN_FAIL);
        }
    }

}
