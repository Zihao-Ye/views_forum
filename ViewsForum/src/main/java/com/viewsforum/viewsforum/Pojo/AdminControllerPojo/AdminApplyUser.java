package com.viewsforum.viewsforum.Pojo.AdminControllerPojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "管理员申请用户")
public class AdminApplyUser {
    @ApiModelProperty(value = "申请用户ID")
    private Integer userID;

    @ApiModelProperty(value = "申请用户名")
    private String userName;

    @ApiModelProperty(value = "申请ID")
    private Integer applyID;

    @ApiModelProperty(value = "申请时间")
    private Timestamp applyTime;
}
