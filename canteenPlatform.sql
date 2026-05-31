-- ==============================================
-- 餐厅微服务平台 - 核心下单业务数据库
-- 精简版：9张表，支持完整下单流程
-- ==============================================

DROP DATABASE IF EXISTS restaurant_platform;
CREATE DATABASE restaurant_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE restaurant_platform;

-- ==============================================
-- 表结构
-- ==============================================

-- 1. 用户表（学生/商家）
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    phone VARCHAR(20) COMMENT '手机号',
    role TINYINT NOT NULL DEFAULT 1 COMMENT '1-学生 2-商家 3-管理员',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 食堂表
CREATE TABLE canteen (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '食堂ID',
    name VARCHAR(50) NOT NULL COMMENT '食堂名称',
    code VARCHAR(20) NOT NULL UNIQUE COMMENT '食堂编码',
    location VARCHAR(100) COMMENT '位置',
    status TINYINT DEFAULT 1 COMMENT '营业状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食堂表';

-- 3. 楼层表
CREATE TABLE floor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼层ID',
    canteen_id BIGINT NOT NULL COMMENT '食堂ID',
    floor_num INT NOT NULL COMMENT '楼层号',
    floor_name VARCHAR(20) NOT NULL COMMENT '楼层名称',
    status TINYINT DEFAULT 1 COMMENT '营业状态 0-歇业 1-营业',
    FOREIGN KEY (canteen_id) REFERENCES canteen(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼层表';

-- 4. 窗口表（商家店铺）
CREATE TABLE  `window` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '窗口ID',
    canteen_id BIGINT NOT NULL COMMENT '食堂ID',
    floor_id BIGINT NOT NULL COMMENT '楼层ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    name VARCHAR(50) NOT NULL COMMENT '窗口名称',
    code VARCHAR(20) NOT NULL UNIQUE COMMENT '窗口编码',
    business_status TINYINT DEFAULT 1 COMMENT '营业状态 0-歇业 1-营业',
    rating DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
    daily_sales INT DEFAULT 0 COMMENT '日销量',
    monthly_sales INT DEFAULT 0 COMMENT '月销量',
    total_sales INT DEFAULT 0 COMMENT '总销量',
    FOREIGN KEY (canteen_id) REFERENCES canteen(id),
    FOREIGN KEY (floor_id) REFERENCES floor(id),
    FOREIGN KEY (merchant_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='窗口表';

-- 5. 菜品分类表
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(30) NOT NULL COMMENT '分类名称',
    sort_order INT DEFAULT 0 COMMENT '排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品分类表';

-- 6. 菜品表
CREATE TABLE dish (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜品ID',
    window_id BIGINT NOT NULL COMMENT '窗口ID',
    name VARCHAR(50) NOT NULL COMMENT '菜品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    image_url VARCHAR(255) COMMENT '图片URL',
    stock INT DEFAULT 0 COMMENT '库存',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热销 0-否 1-是',
    status TINYINT DEFAULT 1 COMMENT '上架状态 0-下架 1-上架',
    daily_sales INT DEFAULT 0 COMMENT '日销量',
    monthly_sales INT DEFAULT 0 COMMENT '月销量',
    total_sales INT DEFAULT 0 COMMENT '总销量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (window_id) REFERENCES `window`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- 7. 菜品-分类关联表（多对多）
CREATE TABLE dish_category (
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    PRIMARY KEY (dish_id, category_id),
    FOREIGN KEY (dish_id) REFERENCES dish(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品分类关联表';

-- 8. 订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    window_id BIGINT NOT NULL COMMENT '窗口ID',
    order_type TINYINT NOT NULL COMMENT '1-自提 2-堂食',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    status TINYINT DEFAULT 1 COMMENT '1-待支付 2-待接单 3-制作中 4-待取餐 5-已完成 6-已取消',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (window_id) REFERENCES `window`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 9. 订单明细表
CREATE TABLE order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    dish_name VARCHAR(50) NOT NULL COMMENT '菜品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (dish_id) REFERENCES dish(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- ==============================================
-- 测试数据
-- ==============================================

-- 用户数据
INSERT INTO sys_user (username, password, nickname, phone, role) VALUES
('merchant01', '123456', '张老板', '13800000001', 2),
('merchant02', '123456', '李老板', '13800000002', 2),
('student01', '123456', '王小明', '13900000001', 1),
('student02', '123456', '张小红', '13900000002', 1);

-- 食堂数据
INSERT INTO canteen (name, code, location) VALUES
('第一食堂', 'HUE01', '学校东边'),
('第二食堂', 'HUE02', '学校中心'),
('第三食堂', 'HUE03', '学校西边');

-- 楼层数据
INSERT INTO floor (canteen_id, floor_num, floor_name) VALUES
(1, 1, '一层'), (1, 2, '二层'), (1, 3, '三层'),
(2, 1, '一层'), (2, 2, '二层'),(1, 3, '三层'),
(3, 1, '一层'), (3, 2, '二层'), (3, 3, '三层');

-- 窗口数据
INSERT INTO `window` (canteen_id, floor_id, merchant_id, name, code, rating, monthly_sales) VALUES
(1, 1, 1, '老北京炸酱面', 'W001', 4.8, 580),
(1, 1, 1, '川味麻辣烫', 'W002', 4.6, 720),
(1, 2, 1, '黄焖鸡米饭', 'W003', 4.7, 650),
(2, 1, 2, '豆浆油条', 'W004', 4.9, 450),
(2, 2, 2, '湘菜小炒', 'W005', 4.5, 380),
(3, 1, 1, '自选快餐', 'W006', 4.4, 820),
(3, 3, 1, '清真拉面', 'W007', 4.8, 520);

-- 菜品分类
INSERT INTO category (name, sort_order) VALUES
('主食', 1), ('炒菜', 2), ('面食', 3), ('小吃', 4), ('饮品', 5);

-- 菜品数据
INSERT INTO dish (window_id, name, price, stock, is_hot, monthly_sales) VALUES
-- 炸酱面窗口
(1, '经典炸酱面', 12.00, 100, 1, 320),
(1, '鸡蛋炸酱面', 14.00, 80, 1, 280),
(1, '牛肉炸酱面', 18.00, 50, 0, 150),
-- 麻辣烫窗口
(2, '招牌麻辣烫', 18.00, 200, 1, 450),
(2, '微辣麻辣烫', 18.00, 150, 0, 380),
(2, '番茄麻辣烫', 18.00, 100, 0, 200),
-- 黄焖鸡窗口
(3, '黄焖鸡米饭', 20.00, 150, 1, 400),
(3, '黄焖排骨米饭', 25.00, 80, 0, 250),
-- 豆浆油条
(4, '现磨豆浆', 3.00, 300, 1, 600),
(4, '香脆油条', 2.00, 200, 1, 550),
-- 湘菜
(5, '农家小炒肉', 18.00, 60, 1, 220),
(5, '酸辣土豆丝', 12.00, 80, 0, 180),
-- 自选快餐
(6, '糖醋里脊', 15.00, 100, 1, 380),
(6, '宫保鸡丁', 16.00, 90, 1, 420),
-- 清真拉面
(7, '牛肉拉面', 20.00, 100, 1, 380),
(7, '羊肉串', 3.00, 200, 1, 450);

-- 菜品-分类关联（多对多）
INSERT INTO dish_category (dish_id, category_id) VALUES
-- 炸酱面（面食+主食）
(1, 3), (1, 1),
(2, 3), (2, 1),
(3, 3), (3, 1),
-- 麻辣烫（小炒+炒菜）
(4, 4), (4, 2),
(5, 4), (5, 2),
(6, 4), (6, 2),
-- 黄焖鸡（主食+炒菜）
(7, 1), (7, 2),
(8, 1), (8, 2),
-- 豆浆（饮品）
(9, 5),
-- 油条（主食+小吃）
(10, 1), (10, 4),
-- 湘菜（炒菜）
(11, 2),
(12, 2),
-- 快餐（炒菜+主食）
(13, 2), (13, 1),
(14, 2), (14, 1),
-- 拉面（面食）
(15, 3),
-- 羊肉串（小吃）
(16, 4);

-- 测试订单
INSERT INTO orders (order_no, user_id, window_id, order_type, total_amount, pay_amount, status) VALUES
('ORD202401150001', 3, 1, 1, 26.00, 26.00, 5);

-- 订单明细
INSERT INTO order_item (order_id, dish_id, dish_name, price, quantity, subtotal) VALUES
(1, 1, '经典炸酱面', 12.00, 1, 12.00),
(1, 9, '现磨豆浆', 3.00, 2, 6.00),
(1, 10, '香脆油条', 2.00, 4, 8.00);