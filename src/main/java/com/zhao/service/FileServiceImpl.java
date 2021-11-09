package com.zhao.service;

import com.zhao.dao.FileInfoMapper;
import com.zhao.pojo.FileInfo;

public class FileServiceImpl implements FileService {
    private FileInfoMapper fileInfoMapper;

    public void setFileInfoMapper(FileInfoMapper fileInfoMapper) {
        this.fileInfoMapper = fileInfoMapper;
    }

    @Override
    public int addFileInfo(FileInfo fileInfo) {
        return fileInfoMapper.addFileInfo(fileInfo);
    }

    @Override
    public FileInfo selectFile(String uuid) {
        return fileInfoMapper.selectFile(uuid);
    }

}
