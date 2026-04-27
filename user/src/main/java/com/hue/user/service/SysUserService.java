package com.hue.user.service;

import com.hue.common.dto.LoginDTO;
import com.hue.common.dto.RegisterDTO;
import com.hue.common.vo.LoginVO;
import com.hue.user.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 21991
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2026-04-22 22:16:06
*/
public interface SysUserService extends IService<SysUser> {
    LoginVO login(LoginDTO loginDTO);
    boolean register(RegisterDTO registerDTO);
    List<SysUser> getDisabled();

}
