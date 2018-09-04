package com.fayayo.job.common.util;

import com.fayayo.job.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

/**
 * @author dalizu on 2018/9/4.
 * @version v1.0
 * @desc 外部脚本调用
 */
@Slf4j
public class ShellCall {
    private static final String BREAK_LINE;//换行

    private static final byte[] EXIT_COMMAND;

    private static byte[] ERROR_BUFFER;

    /**
     * 静态变量初始化
     */
    static {
        BREAK_LINE = "\n";
        EXIT_COMMAND = "\nexit\n".getBytes();
        ERROR_BUFFER = new byte[32];
    }

     /**
       *@描述 运行命令
     */
    public static void runCommand(List<String> list) {
       log.info("{}准备执行脚本命令.....", Constants.LOG_PREFIX);
        Process start = null;
        InputStream in = null;
        OutputStream out = null;
        InputStream errIn = null;
        StringBuilder resultMsg = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder().redirectErrorStream(true).command(list);
            start = processBuilder.start();
            log.info("{}启动脚本脚本命令.....", Constants.LOG_PREFIX);
            out = start.getOutputStream();
            in = start.getInputStream();
            errIn = start.getErrorStream();
            out.write(EXIT_COMMAND);
            out.flush();
            log.info("{}执行退出命令.....", Constants.LOG_PREFIX);

            inputStreamReader = new InputStreamReader(in);//获取输入流
            bufferedReader = new BufferedReader(inputStreamReader);

            String str;
            resultMsg = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                resultMsg.append(str);
                resultMsg.append(BREAK_LINE);//换行
            }

            while ((errIn.read(ERROR_BUFFER)) > 0) {
            }
            //此处注意的是，读取流信息的时候，有可能流对象太大，
            //不能一次性读完，导致获取的字符串顺序错乱或缺失的问题，
            //所以我们等程序执行完毕之后再去读取
            //waitFor会让线程阻塞，直至process执行完毕
            int returnCode = start.waitFor();

            if (returnCode != 0) {
                throw new Exception(resultMsg.toString());
            }
            log.info("{}执行脚本文件完毕.....", Constants.LOG_PREFIX);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行脚本命令失败,{}",e);
        } finally {
            closeAll(out, errIn, in, inputStreamReader, bufferedReader);
            if (start != null) {
                start.destroy();
                start = null;
            }
        }
    }

    private static void closeAll(OutputStream out, InputStream errIn, InputStream in, InputStreamReader inputStreamReader, BufferedReader bufferedReader) {
        if (out != null)
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (errIn != null)
            try {
                errIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (in != null)
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (inputStreamReader != null)
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (bufferedReader != null)
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
