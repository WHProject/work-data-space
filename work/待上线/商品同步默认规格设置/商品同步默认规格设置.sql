USE zntg_jxweb;

# 1.商品同步默认规格设置
ALTER TABLE `core_goods`
MODIFY COLUMN `Ctn`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1*1' COMMENT '箱规' AFTER `Degree`;