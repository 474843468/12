package com.example.taojin.qq12.utils;

/**
 * Created by taojin on 2016/9/8.16:57
 */
public class StringUtils {
    public static boolean checkUsername(String username){

        if (username==null){
            return  false;
        }
        return username.matches("^[a-zA-Z]\\w{2,19}$");
    }

    public static boolean checkPwd(String pwd){

        if (pwd==null){
            return  false;
        }
        return pwd.matches("^[0-9]{3,20}$");
    }

    public static String getInital(String username){
        if (username ==null){
            return "";
        }
        return  username.substring(0,1).toUpperCase();
    }
}
