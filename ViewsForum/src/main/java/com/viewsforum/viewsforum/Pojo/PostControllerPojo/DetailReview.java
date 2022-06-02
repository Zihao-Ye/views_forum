package com.viewsforum.viewsforum.Pojo.PostControllerPojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "回复信息")
public class DetailReview {
    @ApiModelProperty(value = "回复ID")
    private Integer reviewID;

    @ApiModelProperty(value = "回复内容")
    private String reviewContent;

    @ApiModelProperty(value = "回复种类", notes = "1：回复评论 2：回复回复")
    private Integer reviewType;

    @ApiModelProperty(value = "回复时间")
    private Timestamp reviewTime;

    @ApiModelProperty(value = "回复者ID")
    private Integer fromID;

    @ApiModelProperty(value = "回复者名字")
    private String fromName;

    @ApiModelProperty(value = "被回复者ID")
    private Integer toID;

    @ApiModelProperty(value = "被回复者名字")
    private String toName;
}
