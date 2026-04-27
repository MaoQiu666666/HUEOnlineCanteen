package com.hue.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OrderNoUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random RANDOM = new Random();

    /**
     * 生成订单号：时间戳(14) + 用户ID后3位 + 随机数(2位)
     * @param userId 用户ID
     * @return 订单号
     */
    public static String generateOrderNo(Long userId) {
        // 1. 14位时间戳：yyyyMMddHHmmss
        String timeStr = LocalDateTime.now().format(FORMATTER);
        // 2. 用户ID后3位，不足补0
        String userIdSuffix = String.format("%03d", userId % 1000);
        // 3. 2位随机数
        String randomStr = String.format("%02d", RANDOM.nextInt(100));
        // 拼接
        return timeStr + userIdSuffix + randomStr;
    }
}