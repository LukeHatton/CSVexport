package com.example.csvexport.mapper;

import com.example.csvexport.bean.Page;
import com.example.csvexport.bean.TestTable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Project: CSVexport
 * <p>Description:
 *
 * @author lizhao 2021/11/27
 */
@Component
public interface TestTableMapper {
    List<TestTable> selectAll(Page page);
}
