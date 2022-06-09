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
@Table(name = "post")
@ApiModel(description = "帖子")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "帖子ID")
    private Integer postID;

    @ApiModelProperty(value = "发帖者ID")
    private Integer createID;

    @ApiModelProperty(value = "帖子所属主题ID")
    private Integer topicID;

    @ApiModelProperty(value = "帖子名")
    private String postName;

    @ApiModelProperty(value = "帖子下评论数")
    private Integer commentNum;

    @ApiModelProperty(value = "贴子发布时间")
    private Timestamp postTime;

    @ApiModelProperty(value = "图片路径，若未上传文件，则此路径存储为‘null’")
    private String picturePath;

    @ApiModelProperty(value = "是否被删除")
    private Integer isDelete;
}
