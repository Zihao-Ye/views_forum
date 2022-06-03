package com.viewsforum.viewsforum.Pojo.AdminControllerPojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "管理员用户")
public class AdminUser {
    @ApiModelProperty(value = "管理员用户ID")
    private Integer userID;

    @ApiModelProperty(value = "管理员用户名")
    private String userName;

    @ApiModelProperty(value = "管理员ID")
    private Integer adminID;

    @ApiModelProperty(value = "管理员种类", notes = "0：系统管理员 1：创建者管理员 2：申请后的管理员")
    private Integer adminType;
}
