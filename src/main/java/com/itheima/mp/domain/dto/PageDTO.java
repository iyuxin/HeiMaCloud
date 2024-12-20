package com.itheima.mp.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "分页结果")
public class PageDTO<T> {

    @ApiModelProperty(value = "总记录数")
    private Long total;
    @ApiModelProperty(value = "总页数")
    private Long pages;
    @ApiModelProperty(value = "集合")
    private List<T> list;

}
