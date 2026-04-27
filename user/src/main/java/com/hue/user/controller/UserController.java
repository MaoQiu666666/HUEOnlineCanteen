package com.hue.user.controller;

import com.hue.common.dto.LoginDTO;
import com.hue.common.dto.RegisterDTO;
import com.hue.common.result.Result;
import com.hue.common.vo.LoginVO;
import com.hue.user.pojo.SysUser;
import com.hue.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = sysUserService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }

    @PostMapping("/register")
    public Result <?> register(RegisterDTO registerDTO) {
        if (!sysUserService.register(registerDTO)){
            return Result.error("注册失败");
        }
        return Result.success("注册成功！");
    }

    @GetMapping("/disabled")
    @PreAuthorize("hasRole('admin')") // 方法级权限控制
    public Result<List<SysUser>> getDisabledMerchants() {
        List<SysUser> list = sysUserService.getDisabled();
        return Result.success("禁用用户列表",list);
    }
}
