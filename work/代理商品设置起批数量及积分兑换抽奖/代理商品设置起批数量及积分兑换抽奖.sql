USE zntg_jxweb;

#1.新增代理商品起批数量字段
ALTER TABLE `agent_product`
ADD COLUMN `initial_wholesale_quantity`  int(11) DEFAULT NULL COMMENT '起批数量' AFTER `rate`;

#2.新增积分兑换抽奖次数相关字段
ALTER TABLE `tg_LuckyDraw`
ADD COLUMN `use_integral_sign`  bit(1) NULL COMMENT '积分抽奖:0=不选中；1=选中' AFTER `LoginSign`,
ADD COLUMN `use_integral`  decimal(10,2) NULL COMMENT '消费积分数量' AFTER `UseMoney`,
ADD COLUMN `use_integral_count`  int(11) NULL COMMENT '消耗积分兑换抽奖次数' AFTER `UseMoneyCount`;