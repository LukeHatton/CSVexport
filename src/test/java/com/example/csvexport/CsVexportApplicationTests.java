package com.example.csvexport;

import com.example.csvexport.bean.Page;
import com.example.csvexport.bean.TestTable;
import com.example.csvexport.mapper.TestTableMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CsVexportApplicationTests {

    @Autowired
    private TestTableMapper testTableMapper;

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

}
