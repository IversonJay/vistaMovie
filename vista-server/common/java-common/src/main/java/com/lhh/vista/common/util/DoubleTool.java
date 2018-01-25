package com.lhh.vista.common.util;

import java.math.BigDecimal;

public class DoubleTool {
    /**
     * 保留两位小数
     *
     * @param d
     * @return
     */
    public static Double format(Double d) {
        if (d == null) {
            return null;
        }
        BigDecimal b = new BigDecimal(d);
        double newDouble = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return newDouble;
    }

}
