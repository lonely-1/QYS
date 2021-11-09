package com.zhao.service;

import com.zhao.pojo.FileInfo;
import org.apache.ibatis.annotations.Param;

public interface FileService {
    public int addFileInfo(FileInfo fileInfo);    //向数据库添加文件信息

    public FileInfo selectFile(@Param("id") String uuid);   //通过uuid进行查询文件
}
