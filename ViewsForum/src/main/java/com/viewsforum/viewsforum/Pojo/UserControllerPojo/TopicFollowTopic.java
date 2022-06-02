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
@ApiModel(description = "关注主题主题")
public class TopicFollowTopic {
    @ApiModelProperty(value = "主题ID")
    private Integer topicID;

    @ApiModelProperty(value = "主题名")
    private String topicName;

    @ApiModelProperty(value = "主题内容")
    private String topicNote;

    @ApiModelProperty(value = "主题关注数")
    private Integer followNum;

    @ApiModelProperty(value = "主题贴子数")
    private Integer postNum;

    @ApiModelProperty(value = "主题关注ID")
    private Integer topicFollowID;

    @ApiModelProperty(value = "关注时间")
    private Timestamp followTime;
}
