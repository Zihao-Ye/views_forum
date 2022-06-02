package com.viewsforum.viewsforum.Pojo.UserControllerPojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "关注用户用户")
public class UserFollowUser {
    @ApiModelProperty(value = "用户ID")
    private Integer userID;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户关注ID")
    private Integer userFollowID;

    @ApiModelProperty(value = "关注时间")
    private Timestamp followTime;
}
