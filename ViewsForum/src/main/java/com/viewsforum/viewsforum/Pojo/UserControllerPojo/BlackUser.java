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
@ApiModel(description = "黑名单用户")
public class BlackUser {
    @ApiModelProperty(value = "被拉黑的用户ID")
    private Integer userID;

    @ApiModelProperty(value = "被拉黑的用户名")
    private String userName;

    @ApiModelProperty(value = "黑名单ID")
    private Integer blackID;

    @ApiModelProperty(value = "拉黑时间")
    private Timestamp blackedTime;
}
