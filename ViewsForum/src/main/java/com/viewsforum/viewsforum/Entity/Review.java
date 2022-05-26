package com.viewsforum.viewsforum.Entity;

import io.swagger.annotations.Api;
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
@Table(name = "review")
@ApiModel(description = "回复")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "回复ID")
    private Integer reviewID;

    @ApiModelProperty(value = "回复所属评论ID")
    private Integer commentID;

    @ApiModelProperty(value = "回复者ID")
    private Integer fromID;

    @ApiModelProperty(value = "被回复者ID")
    private Integer toID;

    @ApiModelProperty(value = "被回复的评论ID/被回复的回复ID")
    private Integer reviewedID;

    @ApiModelProperty(value = "回复内容")
    private String reviewContent;

    @ApiModelProperty(value = "回复种类", notes = "1：回复评论 2：回复回复")
    private Integer reviewType;

    @ApiModelProperty(value = "回复时间")
    private Timestamp reviewTime;

    @ApiModelProperty(value = "是否被删除")
    private Integer isDelete;
}
