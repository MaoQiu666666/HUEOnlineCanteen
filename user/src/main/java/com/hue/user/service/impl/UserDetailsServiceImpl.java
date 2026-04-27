package com.hue.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hue.user.pojo.SysUser;
import com.hue.user.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security认证的核心入口，作用是根据用户名从你的业务数据源里查出用户信息，封装成UserDetails对象交给框架做后续的密码校验、权限加载。
 * 需要注意三点：一是必须抛出UsernameNotFoundException，查不到用户时明确告诉框架“用户不存在”；
 * 二是如果用户有禁用、锁定这类状态，要抛出对应的DisabledException、LockedException，让框架识别账号状态；
 * 三是查到用户后要把加密密码、权限列表都正确封装进UserDetails，别漏了权限信息，不然登录后会出现无权限问题。
 * 另外不用自己写密码比对，框架会用你配置的PasswordEncoder自动完成这步并抛BadCredentialsException。
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectOne(
                Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getUsername,username));

        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }


        if (sysUser.getStatus() == 0) {
            throw new DisabledException("账号已被禁用");
        }

        // 封装成 UserDetails（注意：角色需加 "ROLE_" 前缀）
        return User.withUsername(sysUser.getUsername())
                .password(sysUser.getPassword())
                .authorities("ROLE_" + sysUser.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
