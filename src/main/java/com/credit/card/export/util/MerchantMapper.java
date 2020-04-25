package com.credit.card.export.util;

import com.google.common.collect.Maps;

import java.util.Map;

public class MerchantMapper {

    public static Map<String, String> merchantMap = Maps.newHashMap();
    static {
        merchantMap.put("9055810701103UX", "万豪");
        merchantMap.put("9055810581203ZX", "鸡排");
        merchantMap.put("9055810531103ZG", "客兴商店");
        merchantMap.put("905581053110400", "瑞雪");
        merchantMap.put("9055810531103ZZ", "悦可百货");
        merchantMap.put("9055810701103UW", "湖龙酒楼2");
        merchantMap.put("905581053110423", "小西百货");
    }

}
