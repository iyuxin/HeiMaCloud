package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.AddressMapper;
import com.itheima.mp.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Clive
 * @since 2024-12-18
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Override
    public List<AddressVO> queryAddressById(Long id) {
        User user = Db.getById(id, User.class);

        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户不存在或已被冻结");
        }

        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id).list();

        if (CollUtil.isEmpty(addresses)) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(addresses, AddressVO.class);
    }
}
