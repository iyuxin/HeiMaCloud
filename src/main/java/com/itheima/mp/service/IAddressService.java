package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.vo.AddressVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Clive
 * @since 2024-12-18
 */
public interface IAddressService extends IService<Address> {

    List<AddressVO> queryAddressById(Long id);
}
