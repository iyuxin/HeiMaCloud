package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户管理接口")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @ApiOperation(value = "新增用户接口")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {
        User user = BeanUtil.copyProperties(userFormDTO, User.class);

        userService.save(user);
    }

    @ApiOperation(value = "删除用户接口")
    @DeleteMapping("/{id}")
    public void deleteUserById(@ApiParam("用户id") @PathVariable("id") Long id) {
        userService.removeById(id);
    }

    @ApiOperation(value = "查询用户接口")
    @GetMapping("/{id}")
    public UserVO queryById(@ApiParam("用户id") @PathVariable("id") Long id) {
        User user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @ApiOperation(value = "批量查询用户接口")
    @GetMapping
    public List<UserVO> queryById(@ApiParam("用户id集合") @RequestParam("ids") List<Long> ids) {
        List<User> users = userService.listByIds(ids);
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @ApiOperation(value = "扣减用户余额接口")
    @PutMapping("/{id}/deduction/{money}")
    public void deductBalance(
            @ApiParam("用户id") @PathVariable("id") Long id,
            @ApiParam("扣减的金额") @PathVariable("money") Integer money) {
        userService.deductBalance(id, money);
    }

    @ApiOperation(value = "根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUsers(UserQuery userQuery) {
        List<User> users = userService.queryUsers(userQuery.getName(), userQuery.getStatus(), userQuery.getMinBalance(), userQuery.getMaxBalance());
        return BeanUtil.copyToList(users, UserVO.class);
    }

}
