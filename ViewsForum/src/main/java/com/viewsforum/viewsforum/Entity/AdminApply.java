package com.viewsforum.viewsforum.Entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_apply")
@ApiModel(description = "管理员申请")
public class AdminApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "申请ID")
    private Integer applyID;

    @ApiModelProperty(value = "申请者ID")
    private Integer userID;

    @ApiModelProperty(value = "申请管理的主题ID")
    private Integer topicID;

    @ApiModelProperty(value = "申请时间")
    private Timestamp applyTime;

    @ApiModelProperty(value = "申请是否被处理")
    private Integer isFinish;
}
