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
        String PASSWORD_PATTERN="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$";
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

    public boolean checkNote(String note){
        if(!checkNotNull(note) || note.length()<=0 || note.length()>=101){
            return false;
        }
        return true;
    }

    public boolean checkContent(String review){
        if(!checkNotNull(review) || review.length()<=0 || review.length()>=201){
            return false;
        }
        return true;
    }

    public boolean checkTopicName(String topicName){
        if(!checkNotNull(topicName) || topicName.length()<=0 || topicName.length()>=16){
            return false;
        }
        return true;
    }
    
    public boolean checkPostName(String postName){
        if(!checkNotNull(postName) || postName.length()<=0 || postName.length()>=21){
            return false;
        }
        return true;
    }
    
}
