package com.lhh.vista.common.util;

import java.util.Random;

public class StringTool {
    public static String getRandomString(int length) {
        if (length <= 0) {
            return "";
        }
        char[] randomChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'};
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(randomChar[Math.abs(random.nextInt()) % randomChar.length]);
        }
        return stringBuffer.toString();
    }

    public static String getRandomNum(int length) {
        if (length <= 0) {
            return "";
        }
        char[] randomChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(randomChar[Math.abs(random.nextInt()) % randomChar.length]);
        }
        return stringBuffer.toString();
    }

    public static int string2Int(String sint) {
        try {
            return Integer.parseInt(sint);
        } catch (Exception e) {
            return 0;
        }
    }


}
