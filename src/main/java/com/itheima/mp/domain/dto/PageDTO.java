package com.itheima.mp.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "分页结果")
public class PageDTO<T> {

    @ApiModelProperty(value = "总记录数")
    private Long total;
    @ApiModelProperty(value = "总页数")
    private Long pages;
    @ApiModelProperty(value = "集合")
    private List<T> list;

    public static  <PO,VO> PageDTO<VO> of(Page<PO> p, Class<VO> voClass){
        PageDTO<VO> dto = new PageDTO<>();
        // 设置总记录数
        dto.setTotal(p.getTotal());
        // 设置总页数
        dto.setPages(p.getPages());
        // 如果查询结果为空，则设置列表为空列表
        if (CollUtil.isEmpty(p.getRecords())) {
            dto.setList(Collections.emptyList());
        } else {
            // 否则，将查询结果转换为UserVO对象列表
            dto.setList(BeanUtil.copyToList(p.getRecords(),voClass));
        }
        return dto;
    }

    public static  <PO,VO> PageDTO<VO> of(Page<PO> p, Function<PO,VO> convertor){
        PageDTO<VO> dto = new PageDTO<>();
        // 设置总记录数
        dto.setTotal(p.getTotal());
        // 设置总页数
        dto.setPages(p.getPages());
        // 如果查询结果为空，则设置列表为空列表
        List<PO> records = p.getRecords();
        if (CollUtil.isEmpty(records)) {
            dto.setList(Collections.emptyList());
        } else {
            // 否则，将查询结果转换为UserVO对象列表
            List<VO> collect = records.stream().map(convertor).collect(Collectors.toList());
            dto.setList(collect);
        }
        return dto;
    }

}
