package com.example.csvexport;

import org.junit.jupiter.api.Test;

public class SimpleTest {
    @Test
    public void test01() {
        /*
        平台测试：arm64 m1 pro mac:实测一百万数据，压缩+写入耗时400ms,70MB->2.7MB
        平台测试：x86 cpu i9-9900k:相同的一百万数据，写入+耗时仍然是400ms左右，67.6MB->2.56MB
        */
        // ZIPFileUtil.compress("/Users/lizhao/Downloads/test folder/testdata.csv",
        //         "/Users/lizhao/Downloads/test folder/testdata.zip");
        ZIPFileUtil.compress("E:\\test\\testdata.csv","E:\\test\\testdata.zip");
    }

    @Test
    public void test02(){
    }

}
