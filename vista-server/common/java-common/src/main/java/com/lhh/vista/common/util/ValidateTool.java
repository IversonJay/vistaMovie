package com.lhh.vista.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateTool {
    /**
     * @param phnoeNumer
     * @return
     */
    public static Boolean checkPhone(String phnoeNumer) {
        try {
            Pattern p = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$");
            Matcher m = p.matcher(phnoeNumer);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
