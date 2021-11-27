package com.example.csvexport.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Project: CSVexport
 * <p>Description:分页信息类
 *
 * @author lizhao 2021/11/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    private Integer currentPage;

    private Integer recordsPerPage;

    private Integer totalCount;

    /* between and 开始索引 */
    public Integer getCurrentIndex() {
        //主键从1开始自增
        return currentPage * recordsPerPage + 1;
    }

    /* between and 结束索引 */
    public Integer getEndIndex() {
        //排除重复数据，需要-1——因为between and默认两端都是include的
        return getCurrentIndex() + recordsPerPage - 1;
    }
}
