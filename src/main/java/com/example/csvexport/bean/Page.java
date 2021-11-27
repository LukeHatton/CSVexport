package com.example.csvexport.bean;

import lombok.Builder;
import lombok.Data;

/**
 * <p>Project: CSVexport
 * <p>Description:分页信息类
 *
 * @author lizhao 2021/11/27
 */
@Data
@Builder
public class Page {
    private Integer currentPage;
    private Integer recordsPerPage;
    private Integer totalCount;
}
