package com.lhh.vista.util;

import lombok.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/1/7.
 */
public class PrizeUtil {
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Prize {
        private String id;//奖品id
        private int weight;//奖品权重
    }

    public static Prize getPrizeIndex(List<Prize> prizes) {
        DecimalFormat df = new DecimalFormat("######0.00");
        int random = -1;
        try {
            //计算总权重
            double sumWeight = 0;
            for (Prize p : prizes) {
                sumWeight += p.getWeight();
            }

            //产生随机数
            double randomNumber;
            randomNumber = Math.random();

            //根据随机数在所有奖品分布的区域并确定所抽奖品
            double d1 = 0;
            double d2 = 0;
            for (int i = 0; i < prizes.size(); i++) {
                d2 += Double.parseDouble(String.valueOf(prizes.get(i).getWeight())) / sumWeight;
                if (i == 0) {
                    d1 = 0;
                } else {
                    d1 += Double.parseDouble(String.valueOf(prizes.get(i - 1).getWeight())) / sumWeight;
                }
                if (randomNumber >= d1 && randomNumber <= d2) {
                    random = i;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("生成抽奖随机数出错，出错原因：" + e.getMessage());
        }
        return prizes.get(random);
    }

    public static void main(String[] xx) {
        List<Prize> prizes = new ArrayList<>();
        prizes.add(new Prize("1", 1));
        prizes.add(new Prize("2", 2));
        prizes.add(new Prize("3", 3));
        prizes.add(new Prize("4", 4));
        for (int i = 0; i < 10; i++) {
            System.out.println(PrizeUtil.getPrizeIndex(prizes).getId());
        }
    }
}
