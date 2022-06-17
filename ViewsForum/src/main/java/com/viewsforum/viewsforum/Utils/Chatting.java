package com.viewsforum.viewsforum.Utils;

import lombok.Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Data
public class Chatting {
    private String uid;
    private String toUid;
    private String content;
    private String dataTime;

    private List<String> onlineUser;

    public Chatting(String uid,String content,List<String> onlineUser){
        this.uid=uid;
        this.content=content;
        this.onlineUser=onlineUser;
        this.dataTime=getTime();
    }

    public String  getTime(){
        SimpleDateFormat date = new SimpleDateFormat();
        return date.format(System.currentTimeMillis());
    }
}
