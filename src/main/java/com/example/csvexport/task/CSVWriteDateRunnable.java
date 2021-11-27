package com.example.csvexport.task;

import com.example.csvexport.CSVExportUtil;
import com.example.csvexport.bean.TestTable;
import com.example.csvexport.util.BeanUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * <p>project: CSVexport
 * <p>ClassName: CSVExportRunnable
 * <p>Description:异步任务：CSV文件写数据
 * <p>Date: 11/27/2021 23:34
 *
 * @author : Zhao Li
 */
@Slf4j
public class CSVWriteDateRunnable implements Runnable {
    private final File targetFile;

    /* 任务执行计数，当减至0后主线程解除阻塞 */
    private final CountDownLatch countDownLatch;

    public CSVWriteDateRunnable(File targetFile, CountDownLatch countDownLatch) {
        this.targetFile = targetFile;
        this.countDownLatch = countDownLatch;
    }

    @SneakyThrows
    @Override
    public void run() {
        Queue<List<TestTable>> csvQueue = BeanUtil.getBean("csvQueue", ConcurrentLinkedQueue.class);
        if (!csvQueue.isEmpty()) {
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
            @Cleanup FileOutputStream os = new FileOutputStream(targetFile, true);
            CSVExportUtil.writeData(TestTable.class, csvQueue.poll(), fieldNameList, os);
            os.flush();
            countDownLatch.countDown();
            log.info("批次文件写入完成");
        }
    }
}