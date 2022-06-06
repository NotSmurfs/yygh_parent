package com.sjr.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {
//        设置excel文件路径和文件名称
        String fileName = "E:\\excel\\01.xlsx";
//        构建List集合
       List<UserData> userData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userData.add(new UserData(i,"lucy"+i));
        }
//      调用方法实现操作
        EasyExcel.write(fileName,UserData.class).sheet("用户信息")
                .doWrite(userData);
    }
}
