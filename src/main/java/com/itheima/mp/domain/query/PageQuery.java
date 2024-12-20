package com.itheima.mp.domain.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页查询实体")
public class PageQuery {

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页显示条数")
    private Integer pageSize = 5;

    @ApiModelProperty("排序字段")
    private String orderBy;

    @ApiModelProperty("排序方式")
    private String sortBy;

    @ApiModelProperty("是否升序")
    private Boolean isAsc = true;

    public <T> Page<T> toMpPage(OrderItem... orderItems) {

        Page<T> page = Page.of(pageNo,pageSize);

        // 如果用户查询条件中指定了排序字段
        if (orderBy != null) {
            // 添加排序条件，传入排序字段和排序方式（升序或降序）
            page.addOrder(new OrderItem(orderBy, isAsc));
        } else if (orderItems != null){
            // 如果未指定排序字段，则默认按更新时间降序排序
            page.addOrder(orderItems);
        }

        return page;
    }

    public <T> Page<T> toMpPage(String defaultSortBy, boolean defaultAsc) {
        return toMpPage(new OrderItem(defaultSortBy, defaultAsc));
    }

    public <T> Page<T> toMpPageDefaultSortByCreatTime() {
        return toMpPage(new OrderItem("creat_time", false));
    }

    public <T> Page<T> toMpPageDefaultSortByUpdateTime() {
        return toMpPage(new OrderItem("update_time", false));
    }
}
