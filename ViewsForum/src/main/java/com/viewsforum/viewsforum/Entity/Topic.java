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
@Table(name = "topic")
@ApiModel(description = "主题")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主题ID")
    private Integer topicID;

    @ApiModelProperty(value = "创建主题者ID")
    private Integer createID;

    @ApiModelProperty(value = "主题名")
    private String topicName;

    @ApiModelProperty(value = "主题内容")
    private String topicNote;

    @ApiModelProperty(value = "主题关注数")
    private Integer followNum;

    @ApiModelProperty(value = "主题贴子数")
    private Integer postNum;

    @ApiModelProperty(value = "是否被删除")
    private Integer isDelete;
}
