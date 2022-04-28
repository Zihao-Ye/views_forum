package com.viewsforum.viewsforum.Entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
@ApiModel(description = "管理员")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "管理员ID")
    private Integer adminID;

    @ApiModelProperty("用户ID")
    private Integer userID;

    @ApiModelProperty(value = "管理的主题ID")
    private Integer topicID;

    @ApiModelProperty(value = "管理员种类", notes = "0：系统管理员 1：创建者管理员 2：申请后的管理员")
    private Integer adminType;
}
