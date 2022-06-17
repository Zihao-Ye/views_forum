package com.viewsforum.viewsforum.Controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "测试专用", description = "请不要在此接口上测试，本接口仅用于部分后端测试")
@RestController("/test")
public class TestController {
    @GetMapping("/testline")
    public HashMap<String,String> test(){
        System.out.println("touch successfully");
        HashMap<String,String> map=new HashMap<>();
        try {
            map.put("test","false");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
