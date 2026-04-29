package com.hue.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务错误码枚举
 */
@Getter
@AllArgsConstructor
public enum BusinessErrorCode {
    // ------------------- 用户相关（1xxx）-------------------
    USER_NOT_FOUND(1001, "用户不存在"),
    USERNAME_EXISTS(1002, "用户名已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    ACCOUNT_DISABLED(1004, "账号已被禁用"),

    // ------------------- 菜品相关（2xxx）-------------------
    DISH_NOT_FOUND(2001, "菜品不存在"),
    DISH_STOCK_NOT_ENOUGH(2002, "菜品库存不足"),
    DISH_OFF_SHELF(2003, "菜品已下架"),

    // ------------------- 窗口相关（3xxx）-------------------
    WINDOW_CLOSED(3001, "窗口已歇业"),

    // ------------------- 订单相关（4xxx）-------------------
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态错误"),
    UNKNOWN_ORDER_ACTION(40003,"不支持的订单操作类型！"),

    // ------------------- 权限相关（5xxx）-------------------
    PERMISSION_DENIED(5001, "权限不足"),

    // ------------------- 通用错误（9xxx）-------------------
    PARAM_ERROR(9001, "参数错误"),
    SYSTEM_ERROR(9999, "系统繁忙，请稍后重试");

    private final Integer code;
    private final String message;
}