USE zntg_admin;

#1.店铺结算单功能权限配置

INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1251', '858', '结算单管理', '/settlementStatement/index.htm', '5', '2', '1', '2017-04-25 11:49:20', NULL, '');
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1252', '1251', '运营审核', '/settlementStatement/operation.htm', '1', '3', '1', '2017-04-25 11:49:20', NULL, '');
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1253', '1251', '财务审核', '/settlementStatement/finance.htm', '2', '3', '1', '2017-04-25 11:49:20', NULL, '');
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1256', '1251', '妥投订单', '/settlementStatement/showSettlementDetail.htm', '3', '3', '1', '2017-04-25 11:49:20', NULL, '');
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1257', '1251', '查看结算单', '/settlementStatement/showSettlement.htm', '4', '3', '1', '2017-04-25 11:49:20', NULL, '');
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1258', '1251', '查看结算单明细', '/settlementStatement/showSettlementDetail.htm', '5', '3', '1', '2017-04-25 11:49:20', NULL, '');
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1259', '1251', '商品明细', '/settlementStatement/showSettlementProductDetail.htm', '6', '3', '1', '2017-04-25 11:49:20', NULL, '');
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1260', '1251', '明细下载', '/settlementStatement/exportSettlementDetail.htm', '7', '3', '1', '2017-04-25 11:49:20', NULL, '');
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('1261', '1251', '审核结算单', '/settlementStatement/statementConfirm.htm', '8', '3', '1', '2017-04-25 11:49:20', NULL, '');
