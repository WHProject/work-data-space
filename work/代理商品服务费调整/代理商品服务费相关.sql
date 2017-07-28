USE zntg_jxweb;

#1.新增代理商品服务费扣点字段
ALTER TABLE `agent_product`
ADD COLUMN `rate`  decimal(4,4) NOT NULL DEFAULT 0.0500 COMMENT '加盟商服务费比例' AFTER `product_sn`;

#2.店铺商品SKU数量调整
update tg_Shop t set t.max_insale_count='50' where t.max_insale_count='30';

USE zntg_jxweb;

#2.代理商品服务费扣点调整新增权限
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1299', '1275', '更新加盟商服务费比例', '/proxyManage/product/rate/update.htm', '0', '3', '1', '2017-07-14 16:24:32', NULL, NULL);
