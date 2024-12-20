package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {

        User user = getById(id);

        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户不存在或已被冻结");
        }

        if (user.getBalance() < money) {
            throw new RuntimeException("余额不足");
        }

        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, 2)
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance())
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {

        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }

    @Override
    public UserVO queryUserAndAddressesById(Long id) {
        User user = getById(id);
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户不存在或已被冻结");
        }
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);

        if (CollUtil.isNotEmpty(addresses)) {
            List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
            userVO.setAddresses(addressVOS);
        }
        return userVO;
    }

    @Override
    public List<UserVO> queryUserAndAddressesByIds(List<Long> ids) {
        List<User> users = listByIds(ids);
        if (CollUtil.isEmpty(users)) {
            return Collections.emptyList();
        }

        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());

        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIds)
                .list();

        List<UserVO> userVOS = BeanUtil.copyToList(users, UserVO.class);

        if (CollUtil.isNotEmpty(addresses)) {
            for (UserVO userVO : userVOS) {
                List<Address> collect = addresses.stream().filter(address -> address.getUserId().equals(userVO.getId())).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(collect)) {
                    List<AddressVO> addressVOS = BeanUtil.copyToList(collect, AddressVO.class);
                    userVO.setAddresses(addressVOS);
                }
            }
        }
        return userVOS;
    }


}
