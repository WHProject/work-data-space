USE zntg_order;

#1.新增订单商品优惠券分摊金额、余额分摊金额字段
ALTER TABLE `tg_HaveProduct`
ADD COLUMN `use_coupon_price`  decimal(10,2) NULL COMMENT '优惠券分摊金额' AFTER `shop_service_charge_rate`,
ADD COLUMN `use_balance`  decimal(10,2) NULL COMMENT '余额分摊金额' AFTER `use_coupon_price`;