package com.zhao.controller;

import com.zhao.pojo.FileInfo;
import com.zhao.service.FileServiceImpl;
import com.zhao.utils.ResultTest;
import com.zhao.utils.UUIDTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@ResponseBody
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    @Qualifier("fileServiceImpl")
    private FileServiceImpl fileService;

    //上传文件
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
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
        String fileName = file.getOriginalFilename();

        if (!fileName.equals("")) {
            //获取文件大小、文件类型，原始文件名、创建时间、文件保存目录地址
            long size = file.getSize();
            String filetype = file.getContentType();
            String OriginalFileName = file.getOriginalFilename();
            //获取当前日期
            date = new Date();
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String createtime = simpleDateFormat.format(date);

            String filePath = filepath;
            FileInfo fileInfo = new FileInfo(uuid, size, filetype, OriginalFileName, createtime, filepath);
            fileService.addFileInfo(fileInfo);
            //获取文件后缀,并将后缀名改为小写
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            String newFileName = uuid + "." + suffix;
            try {
                //文件输出流 目录+文件名
                FileOutputStream out = new FileOutputStream(filepath + newFileName);
                out.write(file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("File upload failed");
                return uuid;
            }
            logger.info("File uploaded successfully");
            return uuid;
        } else {
            logger.error("file does not exist");
            return uuid;
        }
    }

    //下载文件
    @GetMapping("/download")
    public String download(@RequestParam("uuid") String uuid, HttpServletResponse response) throws UnsupportedEncodingException {
        //根据uuid获取文件
        FileInfo fileInfo = fileService.selectFile(uuid);
        //判断文件是否存在
        if (fileInfo != null) {
            //获取文件后缀
            String OriginalFileName = fileInfo.getOriginalfilename();
            String suffix = OriginalFileName.substring(OriginalFileName.lastIndexOf(".") + 1).toLowerCase();
            //获取文件名和文件路径
            String filename = fileInfo.getId() + "." + suffix;
            String filePath = fileInfo.getFilepath();
            //获取文件
            File file = new File(filePath + filename);
            logger.info("文件路径:" + filePath + filename);
            //判断文件是否存在
            if (file.exists()) {
                response.setContentType("application/octet-stream");
                response.setHeader("content-type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filename, "utf8"));
                byte[] buffer = new byte[1024];
                OutputStream out = null;
                try {
                    FileInputStream input = new FileInputStream(file);
                    BufferedInputStream buff = new BufferedInputStream(input);
                    out = response.getOutputStream();
                    int i = buff.read(buffer);
                    while (i != -1) {
                        out.write(buffer);
                        i = buff.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("异常状态码:410");
                    return "410";
                }
            }
            return "";
        } else {
            logger.error("异常状态码:410");
            return "410";
        }
    }

    //获取文件元数据
    @GetMapping("/getFileInfo")
    public ResultTest getFileInfo(@RequestParam("uuid") String uuid) {
        //获取元数据
        FileInfo file = fileService.selectFile(uuid);
        if (file != null) {
            ResultTest resultUtil = new ResultTest(file);
            logger.info("元数据信息:" + resultUtil);
            return resultUtil;
        } else {
            ResultTest resultUtil = new ResultTest(200, "未找到该信息");
            return resultUtil;
        }
    }
}
