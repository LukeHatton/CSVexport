package com.example.csvexport.task;

import com.example.csvexport.CSVExportUtil;
import com.example.csvexport.bean.TestTable;
import com.example.csvexport.util.BeanUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>project: CSVexport
 * <p>ClassName: CSVExportRunnable
 * <p>Description:异步任务：CSV文件写数据
 * <p>Date: 11/27/2021 23:34
 *
 * @author : Zhao Li
 */
public class CSVWriteDateRunnable implements Runnable {
    private final File targetFile;

    public CSVWriteDateRunnable(File targetFile) {
        this.targetFile = targetFile;
    }

    @Override
    @SneakyThrows
    public void run() {
        ConcurrentLinkedQueue<List<TestTable>> csvQueue = BeanUtil.getBean("csvQueue", ConcurrentLinkedQueue.class);
        List<String> fieldNameList = Arrays.asList(
                "id",
                "column2",
                "column3",
                "column4",
                "column5",
                "column6",
                "column7",
                "column8",
                "column9"
        );
        @Cleanup FileOutputStream os = new FileOutputStream(targetFile);
        CSVExportUtil.writeData(TestTable.class,csvQueue.poll(),fieldNameList, os);
        os.flush();
    }
}
