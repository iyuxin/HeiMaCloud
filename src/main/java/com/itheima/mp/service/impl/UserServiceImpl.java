package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
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

        @Override
    public PageDTO<UserVO> queryUsersPages(UserQuery userQuery) {
        // 获取用户查询条件中的用户名
        String name = userQuery.getName();
        // 获取用户查询条件中的用户状态
        Integer status = userQuery.getStatus();

        // 创建分页对象，传入当前页码和每页显示的记录数
        Page<User> page = Page.of(userQuery.getPageNo(), userQuery.getPageSize());

        // 如果用户查询条件中指定了排序字段
        if (userQuery.getOrderBy() != null) {
            // 添加排序条件，传入排序字段和排序方式（升序或降序）
            page.addOrder(new OrderItem(userQuery.getOrderBy(), userQuery.getIsAsc()));
        } else {
            // 如果未指定排序字段，则默认按更新时间降序排序
            page.addOrder(new OrderItem("update_time", false));
        }

        // 使用Lambda表达式构建查询条件
        Page<User> p = lambdaQuery()
                // 如果用户名不为空，则添加模糊查询条件
                .like(name != null, User::getUsername, name)
                // 如果用户状态不为空，则添加等于查询条件
                .eq(status != null, User::getStatus, status)
                // 执行分页查询
                .page(page);

        // 创建分页数据传输对象
        PageDTO<UserVO> dto = new PageDTO<>();
        // 设置总记录数
        dto.setTotal(p.getTotal());
        // 设置总页数
        dto.setPages(p.getPages());
        // 如果查询结果为空，则设置列表为空列表
        if (CollUtil.isEmpty(p.getRecords())) {
            dto.setList(Collections.emptyList());
        } else {
            // 否则，将查询结果转换为UserVO对象列表
            dto.setList(BeanUtil.copyToList(p.getRecords(), UserVO.class));
        }
        // 返回分页数据传输对象
        return dto;
    }


}
