package com.viewsforum.viewsforum.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamChecker {

    public ParamChecker(){}

    public boolean checkNotNull(String input){
        return input != null;
    }

    public boolean checkNotNull(Integer input){
        return input!=null;
    }

    public boolean checkUserName(String userName){
        if(!checkNotNull(userName) || userName.length()<=4 || userName.length()>=21){
            return false;
        }
        return true;
    }

    public boolean checkPassword(String password){
        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        pattern=Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean checkEmail(String email){
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN="(^[a-zA-Z0-9]{1,10}@[a-zA-Z0-9]{1,5}\\.[a-zA-Z0-9]{1,5})";
        pattern=Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
