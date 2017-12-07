package com.qixing.utlis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2016/11/16.
 */
public class MyTextUtils {

    /**
     * 输入的是否为数字
     * */
    public static boolean isNumber(String str){
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(str);
        if(m.matches() ){
            return true;
//            Toast.makeText(Main.this,"输入的是数字", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 输入的是否为字母
     * */
    public static boolean isLetter(String str){
        Pattern p=Pattern.compile("[a-zA-Z]");
        Matcher m=p.matcher(str);
        if(m.matches()){
            return true;
//            Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 输入的是否为汉字
     * */
    public static boolean isChineseCharacters(String str){
        Pattern p=Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m=p.matcher(str);
        if(m.matches()){
            return true;
//            Toast.makeText(Main.this,"输入的是汉字", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
