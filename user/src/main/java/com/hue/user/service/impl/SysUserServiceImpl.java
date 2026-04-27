package com.hue.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hue.common.dto.LoginDTO;
import com.hue.common.dto.RegisterDTO;
import com.hue.common.exception.BusinessErrorCode;
import com.hue.common.exception.BusinessException;
import com.hue.common.util.JwtUtil;
import com.hue.common.vo.LoginVO;
import com.hue.user.pojo.SysUser;
import com.hue.user.service.SysUserService;
import com.hue.user.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 21991
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2026-04-22 22:16:06
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public LoginVO login(LoginDTO loginDTO) {
        //内部自动调用UserDetailsService加载用户信息, 认证成功后返回authentication
        authenticationManager.authenticate(
                //Authentication 的一个实现类，专门用于处理‘用户名+密码’形式的登录
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        SysUser sysUser = sysUserMapper.selectOne(
                Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getUsername,loginDTO.getUsername()));
        return new LoginVO(
                jwtUtil.generateToken(sysUser.getId(),sysUser.getUsername(),sysUser.getRole()),
                sysUser.getId(),
                sysUser.getUsername(),
                sysUser.getRole()
        );
    }

    //注册仅支持学生用户，商家用户由管理员注册
    @Override
    public boolean register(RegisterDTO registerDTO) {
        //  校验账号是否已存在
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, registerDTO.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException(BusinessErrorCode.USERNAME_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        SysUser sysUser = new SysUser();
        sysUser.setUsername(registerDTO.getUsername());
        sysUser.setPassword(encodedPassword);
        sysUser.setPhone(registerDTO.getPhone());

        return this.save(sysUser);
    }

    // 查看禁用商家列表（仅ADMIN）
    @Override
    public List<SysUser> getDisabled() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getStatus, 0);
        return sysUserMapper.selectList(wrapper);
    }

}




