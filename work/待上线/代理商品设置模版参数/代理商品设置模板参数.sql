USE zntg_jxweb;

#1.新增代理商品模版字段
ALTER TABLE `agent_product`
ADD COLUMN `agent_price`  decimal(10,2) DEFAULT NULL COMMENT '代理价' AFTER `modify_time`,
ADD COLUMN `kpi_q1`  decimal(10,6) DEFAULT NULL COMMENT '第一考核期考核指标（万元）' AFTER `agent_price`,
ADD COLUMN `kpi_q2`  decimal(10,6) DEFAULT NULL COMMENT '第二考核期考核指标（万元）' AFTER `kpi_q1`,
ADD COLUMN `kpi_q3`  decimal(10,6) DEFAULT NULL COMMENT '第三考核期考核指标（万元）' AFTER `kpi_q2`,
ADD COLUMN `kpi_q4`  decimal(10,6) DEFAULT NULL COMMENT '第四考核期考核指标（万元）' AFTER `kpi_q3`,
ADD COLUMN `kpi_y`  decimal(10,6) DEFAULT NULL COMMENT '年度考核指标（万元）' AFTER `kpi_q4`,
ADD COLUMN `rebate_percentage_q`  decimal(10,2) DEFAULT NULL COMMENT '季度考核指标达到后的返利比例' AFTER `kpi_y`,
ADD COLUMN `rebate_percentage_y`  decimal(10,2) DEFAULT NULL COMMENT '年度考核达标后的返利比例' AFTER `rebate_percentage_q`,
ADD COLUMN `base_num`  int(4) DEFAULT 0 COMMENT '买赠基数' AFTER `rebate_percentage_y`,
ADD COLUMN `give_num`  int(4) DEFAULT 0 COMMENT '赠送数量' AFTER `base_num`,
ADD COLUMN `cycle_give`  bit(1) DEFAULT b'0' COMMENT '是否循环赠送（0:否，1:是）' AFTER `give_num`;

USE zntg_admin;

#2.新增查看代理商品明细权限
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('13014', '1275', '代理商品查看', '/proxyManage/product/detail.htm', '0', '3', '1', '2017-09-25 17:38:58', NULL, NULL);

#3.店铺商品排序
ALTER TABLE `zntg_jxweb`.`tg_Product` ADD COLUMN `sort_shop` INT(11) NULL  COMMENT '店铺商品排序，用于店铺首页商品显示顺序' AFTER `is_free_freight`;