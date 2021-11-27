package com.example.csvexport;

import org.junit.jupiter.api.Test;

public class SimpleTest {
    @Test
    public void test01() {
        //实测一百万数据，压缩+写入耗时400ms
        //应该在x86架构平台上再次测试，排除m1 pro芯片的影响
        //源文件大小70MB，压缩后大小2.7MB
        ZIPFileUtil.compress("/Users/lizhao/Downloads/test folder/testdata.csv",
                "/Users/lizhao/Downloads/test folder/testdata.zip");
    }

    /* TODO 文件压缩的异步版本 */
    @Test
    public void test02(){
    }

}
