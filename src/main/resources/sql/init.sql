-- 使用qg_bank这个数据库
use qg_bank;

-- 创建名为user的用户信息表
create table user
(
    id       int unsigned primary key auto_increment comment '用户唯一标识',
    username varchar(20)    not null unique comment '用户名',
    password varchar(20)    not null default '123456' comment '密码',
    balance  decimal(10, 2) not null default 0 comment '余额'
) comment '用户数据';

-- 创建名为transaction_record的流水记录表
create table transaction_record
(
    id int unsigned primary key auto_increment comment '主键',
    user_id int unsigned not null comment '关联用户id',
    type tinyint not null comment '交易类型(1. 存款 2. 取款 3. 转入 4. 转出)',
    amount decimal(10, 2) not null comment '金额',
    target_user_id int unsigned null comment '交易对象 null: 存/取款',
    create_time datetime not null default current_timestamp comment '订单创建时间'
) comment '流水记录表'