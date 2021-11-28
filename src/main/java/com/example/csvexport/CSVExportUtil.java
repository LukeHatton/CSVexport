package com.example.csvexport;

import com.example.csvexport.util.ReflectUtil;
import lombok.SneakyThrows;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>project: CSVexport
 * <p>ClassName: CSVExportUtil。
 * <p><b>注意，本类中的方法均不会对流作任何flush操作，需要调用者自己决定flush的时机</b>
 * <p>Description:数据库CSV文件导出工具类
 * <p>Date: 11/27/2021 14:33
 *
 * @author : Zhao Li
 */
public class CSVExportUtil {

    public static final String CSV_COLUMN_SEPARATOR = ",";

    public static final String CSV_LINE_SEPARATOR = System.lineSeparator();

    /**
     * 导出CSV文件
     *
     * @param clz      要导出的数据的包装类
     * @param titles   csv文件头
     * @param dataList 数据列表，每个对象为一行数据
     * @param os       输出流
     */
    public static <T> void exportCSV(Class<? extends T> clz, List<String> titles, List<T> dataList, List<String> fieldNameList, OutputStream os) {
        writeTitle(titles, os);
        writeData(clz, dataList, fieldNameList, os);
    }

    /**
     * 写csv文件头。用户需自行调用{@link OutputStream#flush()}
     *
     * @param titles 文件头数据
     * @param os     输出流
     */
    @SneakyThrows
    public static void writeTitle(List<String> titles, OutputStream os) {
        OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(os), StandardCharsets.UTF_8);
        StringBuilder builder = new StringBuilder();

        /* ---------------- 组装表头 ----------------- */
        for (String title : titles) {
            builder.append(title).append(CSV_COLUMN_SEPARATOR);
        }
        String subString = removeLastChar(builder);
        writer.append(subString).append(CSV_LINE_SEPARATOR);
        writer.flush();
    }

    @SneakyThrows
    public static <T> void writeData(Class<? extends T> clz, List<T> dataList, List<String> fieldNameList, OutputStream os) {
        OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(os, 8192 * 10), StandardCharsets.UTF_8);

        //数据非空，则写入数据
        if (dataList != null && dataList.size() > 0) {
            //获取方法引用数组
            List<Method> methodList = ReflectUtil.getMethodList(fieldNameList, clz);
            /* ---------------- 组装数据 ----------------- */
            writeDataToWriter(dataList, methodList, writer);
            //防止数据不完整,进行一次flush
            writer.flush();
        }
    }

    /**
     * 移除StringBuilder生成的最后一个字符
     */
    public static String removeLastChar(StringBuilder builder) {
        String string = builder.toString();
        return string.substring(0, string.length() - 1);
    }

    /**
     * 向输出流里写入数据
     *
     * @param dataList   要写入的数据List
     * @param methodList List参数化类型的方法引用
     * @param writer     输出流
     * @param <T>        参数化类型
     */
    @SneakyThrows
    public static <T> void writeDataToWriter(List<T> dataList, List<Method> methodList, OutputStreamWriter writer) {
        for (Object object : dataList) {
            StringBuilder builder = new StringBuilder();
            for (Method method : methodList) {
                Object result = method.invoke(object);
                builder.append(result.toString()).append(CSV_COLUMN_SEPARATOR);
            }
            String dataToWrite = removeLastChar(builder);
            //像压缩文件中写入数据
            writer.append(dataToWrite).append(CSV_LINE_SEPARATOR);
        }
    }

}
