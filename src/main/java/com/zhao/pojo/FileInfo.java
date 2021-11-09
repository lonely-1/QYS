package com.zhao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private String id; //  UUID
    private long filesize; //文件大小
    private String filetype;  //文件类型
    private String originalfilename; //原始文件名
    private String createtime;  //创建时间
    private String filepath;  //文件保存目录地址
}
