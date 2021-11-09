package com.zhao.dao;


import com.zhao.pojo.FileInfo;

public interface FileInfoMapper {
    public int addFileInfo(FileInfo fileInfo);

    public FileInfo selectFile(String uuid);
}
