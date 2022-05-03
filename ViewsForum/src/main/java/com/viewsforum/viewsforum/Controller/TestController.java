package com.viewsforum.viewsforum.Controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试专用", description = "请不要在此接口上测试，本接口仅用于部分后端测试")
@RestController("/test")
public class TestController {
}
