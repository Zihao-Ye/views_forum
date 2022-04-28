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
@Table(name = "user_follow")
@ApiModel(description = "用户关注")
public class UserFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "用户关注ID")
    private Integer userFollowID;

    @ApiModelProperty(value = "关注者ID")
    private Integer followerID;

    @ApiModelProperty(value = "被关注者ID")
    private Integer followedID;

    @ApiModelProperty(value = "关注时间")
    private Timestamp followTime;
}
