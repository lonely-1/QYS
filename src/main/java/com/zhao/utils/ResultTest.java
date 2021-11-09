package com.zhao.utils;

import com.zhao.pojo.FileInfo;

public class ResultTest {
    private Integer code;
    private String msg;
    private FileInfo data;

    public ResultTest(FileInfo data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    public ResultTest(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FileInfo getData() {
        return data;
    }

    public void setData(FileInfo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code + '\n' +
                ", msg=" + msg + '\n' +
                ", data={" + data.getId() + '\n' +
                data.getFilesize() + '\n' +
                data.getFiletype() + '\n' +
                data.getOriginalfilename() + '\n' +
                data.getCreatetime() + '\n' +
                data.getFilepath() + '\n' +
                '}' + '\n' +
                '}';
    }
}
