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
@Table(name = "topic_follow")
@ApiModel(description = "主题关注")
public class TopicFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主题关注ID")
    private Integer topicFollowID;

    @ApiModelProperty(value = "被关注的主题ID")
    private Integer topicID;

    @ApiModelProperty(value = "关注者ID")
    private Integer followerID;

    @ApiModelProperty(value = "关注时间")
    private Timestamp followTime;
}
