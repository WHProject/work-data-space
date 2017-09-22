USE zntg_admin;

#1.代理商列表导出权限
INSERT INTO `zntg_admin`.`adm_Power` (`ID`, `ParentID`, `PowerName`, `Url`, `Sort`, `Type`, `State`, `CreateTime`, `ModifyTime`, `Ext1`) VALUES ('13013', '1278', '代理商导出', '/proxyManage/proxy/export.htm', '0', '3', '1', '2017-09-22 09:45:23', NULL, NULL);