package com.fayayo.job.manager;

import com.fayayo.job.manager.service.FileService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

/**
 * @author dalizu on 2018/9/13.
 * @version v1.0
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FtpServerTest {

    @Value("${faya-job.upload.tempPath}")
    private String path;

    @Autowired
    private FileService fileService;

    @Test
    public void testUplaod(){

        String abc="{\n" +
                "  \"job\": {\n" +
                "    \"setting\": {\n" +
                "      \"speed\": {\n" +
                "        \"channel\":15\n" +
                "      }\n" +
                "    },\n" +
                "    \"content\": [\n" +
                "      {\n" +
                "        \"reader\": {\n" +
                "          \"name\": \"mysqlreader\",\n" +
                "          \"parameter\": {\n" +
                "            \"username\": \"root\",\n" +
                "            \"password\": \"root123\",\n" +
                "            \"column\": [\n" +
                "              \"id\",\n" +
                "              \"username\",\n" +
                "              \"telephone\"\n" +
                "            ],\n" +
                "            \"splitPk\": \"id\",\n" +
                "            \"connection\": [\n" +
                "              {\n" +
                "                \"table\": [\n" +
                "                  \"user\"\n" +
                "                ],\n" +
                "                \"jdbcUrl\": [\n" +
                "                  \"jdbc:mysql://localhost:3306/datax\"\n" +
                "                ]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        \"writer\": {\n" +
                "          \"name\": \"streamwriter\",\n" +
                "          \"parameter\": {\n" +
                "            \"print\": true,\n" +
                "            \"encoding\": \"UTF-8\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        InputStream inputStream= IOUtils.toInputStream(abc);
        fileService.uploadFile(inputStream,path);


    }


}
