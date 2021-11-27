package com.example.csvexport;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.util.StopWatch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZIPFileUtil {

    /**
     * 进行文件压缩
     * 要求生成的文件必须是utf-8编码格式，否则压缩可能会出错
     *
     * @param sourceFile 源文件路径
     * @param targetFile 目标文件路径
     */
    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void compress(String sourceFile, String targetFile) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        File source = new File(sourceFile);                 //源文件File
        ZipEntry zipEntry = new ZipEntry(sourceFile);       //ZipEntry，通过这个对象可以压缩多个文件
        File zip = new File(targetFile);                    //要生成的zip文件
        if (zip.exists()) zip.delete();                     //若文件已经存在，则先删除
        @Cleanup BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
        @Cleanup ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        out.putNextEntry(zipEntry);                        //将zipEntry放到压缩输出流里


        //缓冲区：10MB
        byte[] buffer = new byte[10 * 1024 * 1024];
        int len;
        //不要忘记要读到缓冲区中，否则会写入一个超大的白文件
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        stopWatch.stop();
        System.out.println("成功生成压缩文件！path=" + targetFile);
        System.out.println("生成文件共耗时(ms)：" + stopWatch.getTotalTimeMillis());
        System.out.println(stopWatch.prettyPrint());
    }

}
