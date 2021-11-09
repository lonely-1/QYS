package com.zhao;


import com.zhao.pojo.FileInfo;
import com.zhao.service.FileServiceImpl;
import com.zhao.utils.ResultTest;
import com.zhao.utils.UUIDTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class ControllerTest {
    @Autowired
    @Qualifier("fileServiceImpl")
    private FileServiceImpl fileService;

    @Test
    public void upload() throws IOException {
        File fileup = new File("C:\\Users\\Lonely\\Desktop\\hello.txt");

        InputStream inputStream = new FileInputStream(fileup);
        MultipartFile file = new MockMultipartFile(fileup.getName(), inputStream);
        //设置日期格式"yyyyMMdd",目录格式是日期格式
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String filepath = simpleDateFormat.format(date) + "/";
        //判断目录是否存在.不存在,则创建
        File targetFile = new File(filepath);
        if (!targetFile.exists()) {
            targetFile.mkdir();
        }
        //生成UUID
        String uuid = UUIDTest.getUUID();

        String fileName = file.getName();

        //获取文件相关信息
        if (!fileName.equals("")) {
            //获取文件大小、文件类型，原始文件名、创建时间、文件保存目录地址
            long size = file.getSize();
            String filetype = file.getContentType();
            String OriginalFileName = file.getOriginalFilename();
            //获取当前日期
            date = new Date();
            //日期格式化
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String createtime = simpleDateFormat.format(date);
            //获取文件保存目录地址
            String filePath = filepath;
            //将文件信息等元数据记录至数据库中
            FileInfo fileInfo = new FileInfo(uuid, size, filetype, OriginalFileName, createtime, filepath);
            fileService.addFileInfo(fileInfo);
            //获取文件后缀,并将后缀名改为小写
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            //生成最新的文件名
            String newFileName = uuid + "." + suffix;
            try {
                //文件输出流 目录+文件名
                FileOutputStream out = new FileOutputStream(filepath + newFileName);
                //写入数据
                out.write(file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
                //打印日志
                System.out.println("File upload failed");
                return;
            }
            System.out.println("File uploaded successfully     " + uuid);
            return;
        } else {
            System.out.println("file does not exist    " + uuid);
            return;
        }
    }

    @Test
    public void download() throws IOException {
        //根据uuid获取文件
        String uuid = "b277b639fc6347bdad81311a9980ca56";
        FileInfo fileInfo = fileService.selectFile(uuid);
        //判断文件是否存在
        if (fileInfo != null) {
            //获取文件后缀
            String OriginalFileName = fileInfo.getOriginalfilename();
            System.out.println(OriginalFileName);
            String suffix = OriginalFileName.substring(OriginalFileName.lastIndexOf(".") + 1).toLowerCase();
            //获取文件名和文件路径
            String filename = fileInfo.getId() + "." + suffix;
            String filePath = fileInfo.getFilepath();
            //获取文件
            File file = new File(filePath + filename);
            System.out.println("文件路径:" + filePath + filename);
            //判断文件是否存在
            if (file.exists()) {
                byte[] buffer = new byte[1024];
                //输出流
                try {
                    //将文件内容打印在控制台
                    FileReader fr = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fr);
                    String str = reader.readLine();
                    while (str != null) {
                        System.out.println(str);
                        str = reader.readLine();
                    }
                } catch (FileNotFoundException e) {

                }
            }
        }
    }

    @Test
    public void getFileInfo() {
        String uuid = "df97066b083c4d6d93b42d9285a33192";
        //获取元数据信息
        FileInfo file = fileService.selectFile(uuid);
        System.out.println(file);
        if (file != null) {
            //返回JSON格式
            ResultTest resultUtil = new ResultTest(file);
            System.out.println("元数据信息:" + resultUtil);
        } else {
            //返回JSON格式
            ResultTest resultUtil = new ResultTest(200, "未找到该信息");
            System.out.println("元数据信息:" + resultUtil);
        }
    }
}
