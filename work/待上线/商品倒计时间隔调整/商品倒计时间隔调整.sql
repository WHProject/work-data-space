USE zntg_jxweb;

# 1.历史商品倒计时间隔调整为10年
update tg_Product t set t.endTime = DATE_ADD(t.startTime, INTERVAL 10 YEAR) where t.startTime is not null;