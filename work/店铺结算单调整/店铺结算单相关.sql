USE zntg_jxweb;

#1.审核通过的全部商家初始化商家默认扣点均为3%加盟商为0%。系统默认值。无其他扣点规则。
INSERT INTO shop_rate_set
  (shop_id,merchant_id,rule_type,rate,effective_rate,start_time,end_time,status,creator,create_time)
  SELECT s.ID,m.ID,1,IF(m.type = 1,0,0.03),IF(m.type = 1,0,0.03),NOW(),'3000-12-31 00:00:00',1,0,NOW()
    FROM tg_Merchant m
    LEFT JOIN tg_Shop s
      ON m.ID = s.tg_MerchantID
   WHERE m.State = 1
     AND s.State = 1;

#2.店铺扣点相关表

CREATE TABLE `shop_rate_set` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `shop_id` int(11) NOT NULL COMMENT '店铺id',
  `merchant_id` int(11) NOT NULL COMMENT '商家id',
  `rule_type` tinyint(4) NOT NULL COMMENT '规则类型（1：店铺扣点，2：考核扣点，3：商品扣点）',
  `product_id` int(11) DEFAULT NULL COMMENT '源商品id',
  `rate` varchar(50) NOT NULL COMMENT '达成扣点',
  `effective_rate` decimal(10,2) DEFAULT NULL COMMENT '有效扣点',
  `assess_amount` varchar(50) DEFAULT NULL COMMENT '考核金额',
  `assess_start_time` datetime DEFAULT NULL COMMENT '考核开始时间',
  `assess_end_time` datetime DEFAULT NULL COMMENT '考核结束时间',
  `start_time` datetime DEFAULT NULL COMMENT '生效时间',
  `end_time` datetime DEFAULT NULL COMMENT '失效时间',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '审核状态（0：运营新增，1：审核通过，2：审核驳回，3：作废）',
  `creator` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店铺扣点设置表';

CREATE TABLE `shop_rate_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `shop_rate_id` int(11) NOT NULL COMMENT '店铺扣点规则id',
  `status` tinyint(4) NOT NULL COMMENT '审核状态（0：运营新增，1：审核通过，2：审核驳回）',
  `operator_id` int(11) NOT NULL COMMENT '操作人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_shop_rate_id` (`shop_rate_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='店铺扣点审核表';

#3.店铺结算单相关表

CREATE TABLE `shop_settlement` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `user_id` int(11) NOT NULL COMMENT '用户ID（商家用户ID）',
  `shop_id` int(11) NOT NULL COMMENT '店铺表ID',
  `merchant_id` int(11) NOT NULL COMMENT '商家表ID',
  `sn` varchar(30) NOT NULL COMMENT '店铺结算单编号',
  `pay_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '结算单订单总金额',
  `poundage` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '平台服务费',
  `settlement_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '应结算金额',
  `pay_settlement_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实结金额',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '审核状态：1：生成结算单；2：商家确认；3：运营审核；4：财务审核；5：已结算；6：无需结算；',
  `settlement_time` date NOT NULL COMMENT '结算日期',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已删除 0：未删除，1：已删除',
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`) USING BTREE,
  KEY `index_merchant_id` (`merchant_id`) USING BTREE,
  KEY `index_sn` (`sn`) USING BTREE,
  KEY `index_status` (`status`) USING BTREE,
  KEY `index_settlement_time` (`settlement_time`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='店铺结算单';

CREATE TABLE `shop_settlement_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `shop_settlement_id` int(11) NOT NULL COMMENT '店铺结算单ID',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '审核状态：1：生成结算单；2：商家确认；3：运营审核；4：财务审核；5：已结算；6：无需结算；',
  `auditor_id` int(11) NOT NULL COMMENT '审核人ID',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已删除 0：未删除，1：已删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `index_shop_settlement_id` (`shop_settlement_id`) USING BTREE,
  KEY `index_status` (`status`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='店铺结算单状态变更日志';

ALTER TABLE `order_settlement`
ADD COLUMN `shop_settlement_id`  int(11) NULL COMMENT '订单所属店铺结算单Id' AFTER `bounty_statement_id`,
ADD COLUMN `have_shop_settlement`  bit(1) NOT NULL DEFAULT b'0' COMMENT '是否生成结算单' AFTER `shop_settlement_id`;

ALTER TABLE `shop_statement`
COMMENT='店铺对账单（已弃用）';

USE zntg_admin;

#4.店铺扣点设置功能权限配置
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1262', '850', '店铺扣点管理', '/shopRatesSet/list.htm', '1', '2', '1', '2017-04-26 11:49:20', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1263', '850', '店铺扣点审核列表', '/shopRatesSet/audit/list.htm', '2', '2', '1', '2017-04-27 15:47:31', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1264', '1262', '编辑扣点', '/shopRatesSet/detail.htm', '3', '3', '1', '2017-04-27 15:52:12', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1265', '1262', '查看店铺扣点列表', '/shopRatesSet/detail/list.htm', '4', '3', '1', '2017-04-27 15:52:16', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1266', '1262', '新增规则', '/shopRatesSet/detail/create.htm', '5', '3', '1', '2017-04-27 15:52:18', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1267', '1262', '保存信息规则', '/shopRatesSet/detail/save.htm', '6', '3', '1', '2017-04-27 15:52:20', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1268', '1263', '审核扣点', '/shopRatesSet/audit/detail.htm', '7', '3', '1', '2017-04-27 16:12:30', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1269', '1263', '审核下一条扣点', '/shopRatesSet/audit/next.htm', '8', '3', '1', '2017-04-27 16:14:20', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1270', '1263', '保存审核信息', '/shopRatesSet/audit/save.htm', '9', '3', '1', '2017-04-27 16:14:22', NULL, NULL);
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1272', '1266', '搜索商品', '/shopRatesSet/goods/query.htm', '10', '4', '1', '2017-05-11 09:17:40', NULL, NULL);