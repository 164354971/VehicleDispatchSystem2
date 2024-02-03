package cn.lingbaocrisps.common.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexConstants {
    public static final String USERNAME = "^[\\u4E00-\\u9FA5A-Za-z0-9_]{6,16}$";
    public static final String PASSWORD = "[a-zA-Z0-9]{6,32}";

    /**
     * 字符串匹配
     * @param pattern 匹配格式
     * @param str 匹配的字符串
     * @return flag 返回是否匹配
     */
    public static boolean isMatch(String pattern, String str){
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(str);
        boolean flag = matcher.matches();
        return flag;
    }
}
