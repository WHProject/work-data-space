USE zntg_jxweb;

#1.新增代理商品起批数量字段
ALTER TABLE `agent_product`
ADD COLUMN `initial_wholesale_quantity`  int(11) DEFAULT NULL COMMENT '起批数量' AFTER `rate`;

#2.新增积分兑换抽奖次数相关字段
ALTER TABLE `tg_LuckyDraw`
ADD COLUMN `use_integral_sign`  bit(1) DEFAULT b'0' COMMENT '积分抽奖:0=不选中；1=选中' AFTER `LoginSign`,
ADD COLUMN `use_integral`  decimal(10,2) NULL COMMENT '消费积分数量' AFTER `UseMoney`,
ADD COLUMN `use_integral_count`  int(11) NULL COMMENT '消耗积分兑换抽奖次数' AFTER `UseMoneyCount`;

#3.消耗获得积分比例调整字段大小
ALTER TABLE `tg_Product`
MODIFY COLUMN `UsePointRate`  decimal(10,4) NOT NULL DEFAULT 0.0000 COMMENT '消耗积分比例' AFTER `FirstOnSaleTime`,
MODIFY COLUMN `GivePointRate`  decimal(10,4) NOT NULL DEFAULT 0.0000 COMMENT '赠送积分比例' AFTER `UsePointRate`;

USE zntg_user;

#4.新增积分抽奖消耗获得改变类型注释
ALTER TABLE `tg_UserAccoutChangeDetail`
MODIFY COLUMN `ChangeType`  tinyint(4) NULL DEFAULT 99 COMMENT '变更类型（1.购物，2.签到，3.系统发放，4.手动增加，5.退款（增加），6.取消订单，99.其他；101.用户等级过期，102.退货（扣减），103.消费（购物），104.取消订单，105.手动扣减，106.余额转出，107.兑换，108.抽奖获得积分，109.抽奖消耗积分）' AFTER `adm_UsersID`;