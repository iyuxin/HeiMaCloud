package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public void deductBalance(Long id, Integer money) {

        User user = getById(id);

        if(user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户不存在或已被冻结");
        }

        if(user.getBalance() < money) {
            throw new RuntimeException("余额不足");
        }

        baseMapper.deductBalance(id, money);

    }
}
