package com.fayayo.job.common.util;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Data
public class FtpUtil {

   /* private  static String ftpIP=PropertiesUtil.getProperty("ftp.server.ip");
    private  static String ftpUser=PropertiesUtil.getProperty("ftp.user");
    private  static String ftpPassword=PropertiesUtil.getProperty("ftp.pass");*/
    private  static String ftpIP="192.168.88.128";
    private  static String ftpUser="root";
    private  static String ftpPassword="root123";

    private static String ftpPath="";//上传路径

    private String ip;
    private Integer port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FtpUtil(String ip, Integer port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FtpUtil ftpUtil=new FtpUtil(ftpIP,21,ftpUser,ftpPassword);
        log.info("开始连接ftp,ftpIP:{},ftpUser:{},ftpPassword:{}",ftpIP,ftpUser,ftpPassword);
        boolean result=ftpUtil.uploadFile(ftpPath,fileList);
        log.info("结束上传");
        return result;
    }

    private boolean uploadFile(String remotePath,List<File>fileList) throws IOException {
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
                    System.out.println("上传文件:"+fileItem.getName());
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


    //测试
    public static void main(String []args){
        FtpUtil f=new FtpUtil(ftpIP,21,ftpUser,ftpPassword);
        File targetFile=new File("D:\\Test","2018-06-29-110.txt");
        try {
            f.uploadFile("/root/ftpdata", Lists.newArrayList(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
