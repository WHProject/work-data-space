#导出线上商户信息
select t.ID,t.`Name`,t1.LogoImgPath from tg_Merchant t left JOIN tg_Shop t1 on t.ID=t1.tg_MerchantID where t.State='1';

#商户接待组映射配置
insert into tg_ShopServiceSetting (shop_id,shop_name,shop_logo,setting_id,seller_id) values('970','宿迁市洋河镇国府酒业有限公司','http://img.zhongniang.com/bill/2017/0427/cd0f7861f1214659a57c15b8500e80e0.jpg','jt_970_9999','jt_970');