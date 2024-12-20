package com.itheima.mp.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页查询实体")
public class PageQuery {

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("每页显示条数")
    private Integer pageSize;

    @ApiModelProperty("排序字段")
    private String orderBy;

    @ApiModelProperty("排序方式")
    private String sortBy;

    @ApiModelProperty("是否升序")
    private Boolean isAsc;

}
