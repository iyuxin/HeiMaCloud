package com.itheima.mp.controller;


import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.service.IAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Clive
 * @since 2024-12-18
 */
@Api(tags = "地址管理接口")
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final IAddressService addressService;

    @ApiOperation(value = "根据用户id查询收货地址")
    @GetMapping("/{id}")
    public List<AddressVO> queryAddressById(@ApiParam("用户id") @PathVariable("id") Long id) {
        return addressService.queryAddressById(id);
    }
}
