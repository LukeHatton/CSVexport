package com.example.csvexport;

import com.example.csvexport.bean.Page;
import com.example.csvexport.bean.TestTable;
import com.example.csvexport.mapper.TestTableMapper;
import com.example.csvexport.task.CSVWriteDateRunnable;
import com.example.csvexport.util.BeanUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class CsVexportApplicationTests {

    @Autowired
    private TestTableMapper testTableMapper;

    /* 线程池。希望按顺序写入文件，于是使用单线程写入 */
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    @Test
    void contextLoads() {
    }

    /* 测试springboot集成mybatis进行查询 */
    @Test
    public void test01() {
        //查询第一页，20条数据
        Page page = Page.builder().currentPage(0).recordsPerPage(20).build();
        List<TestTable> testTables = testTableMapper.selectAll(page);
        System.out.println("查询成功");
    }

    /* 从数据库中读取文件写入csv中 */
    @SneakyThrows
    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void test02() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        /* ---------------- 创建文件 ----------------- */
        //File file = new File("E:\\test\\testdata.csv");
        File file = new File("/Users/lizhao/Downloads/test folder/testdata.csv");
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        }
        FileOutputStream os = new FileOutputStream(file);

        /* ---------------- 封装查询参数 ----------------- */
        //数据头
        List<String> titles = Arrays.asList(
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
        //写入数据头
        CSVExportUtil.writeTitle(titles, os);
        os.close();
        log.info("写入文件头数据：成功！");

        /* ---------------- 执行查询 ----------------- */
        Integer countAll = testTableMapper.countAll();
        int recordsPerPage = 50000;
        CountDownLatch countDownLatch = new CountDownLatch(countAll / recordsPerPage + 1);      //异步任务计数器
        for (int i = 0; i < countAll / recordsPerPage + 1; i++) {
            Page page = new Page(i, recordsPerPage, countAll);
            log.info("==>当前页码：【{}】 开始索引：【{}】 结束索引：【{}】", i, page.getCurrentIndex(), page.getEndIndex());
            //==>分页查询并写入文件
            List<TestTable> testTableList = testTableMapper.selectAll(page);
            Queue<List<TestTable>> csvQueue = BeanUtil.getBean("csvQueue", ConcurrentLinkedQueue.class);
            csvQueue.add(testTableList);
            //==>调用异步任务写入文件
            executorService.scheduleAtFixedRate(new CSVWriteDateRunnable(file, countDownLatch), 0, 50, TimeUnit.MILLISECONDS);
        }
        //主线程在此阻塞，等待异步任务完成，在countDownLatch减至0后会解除阻塞.
        countDownLatch.await(8000, TimeUnit.MILLISECONDS);                  //线程超时：8000毫秒
        stopWatch.stop();
        log.info("文件写入完成，共耗时(ms)：" + stopWatch.getTotalTimeMillis());
        ZIPFileUtil.compress("/Users/lizhao/Downloads/test folder/testdata.csv",
                "/Users/lizhao/Downloads/test folder/testdata.zip");
    }

    /* 从数据流中直接写入压缩文件 */
    @SneakyThrows
    @Test
    public void test03(){
        FileOutputStream os = new FileOutputStream("/Users/lizhao/Downloads/test folder/testdata.zip");
        //数据头
        List<String> titles = Arrays.asList(
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
        //写入数据头
        CSVExportUtil.writeTitle(titles, os);
        os.close();
        log.info("写入文件头数据：成功！");
        /* ---------------- 执行查询 ----------------- */
        Integer countAll = testTableMapper.countAll();
        int recordsPerPage = 50000;
        CountDownLatch countDownLatch = new CountDownLatch(countAll / recordsPerPage + 1);      //异步任务计数器
        for (int i = 0; i < countAll / recordsPerPage + 1; i++) {
            Page page = new Page(i, recordsPerPage, countAll);
            log.info("==>当前页码：【{}】 开始索引：【{}】 结束索引：【{}】", i, page.getCurrentIndex(), page.getEndIndex());
            //==>分页查询并写入文件
            List<TestTable> testTableList = testTableMapper.selectAll(page);
            ZIPFileUtil.compressFromList(TestTable.class, testTableList,titles,"/Users/lizhao/Downloads/test folder/testdata.zip");
            ////Queue<List<TestTable>> csvQueue = BeanUtil.getBean("csvQueue", ConcurrentLinkedQueue.class);
            //csvQueue.add(testTableList);
            ////==>调用异步任务写入文件
            //executorService.scheduleAtFixedRate(new CSVWriteDateRunnable(file, countDownLatch), 0, 50, TimeUnit.MILLISECONDS);
        }
    }

}
