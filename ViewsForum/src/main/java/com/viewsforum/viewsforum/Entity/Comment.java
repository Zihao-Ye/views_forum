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
@Table(name = "comment")
@ApiModel(description = "帖子下的评论")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "评论ID")
    private Integer commentID;

    @ApiModelProperty(value = "评论者ID")
    private Integer createID;

    @ApiModelProperty(value = "被评论的帖子ID")
    private Integer postID;

    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    @ApiModelProperty(value = "评论时间")
    private Timestamp commentTime;

    @ApiModelProperty(value = "评论下回复数")
    private Integer reviewNum;

    @ApiModelProperty(value = "图片路径，若未上传文件，则此路径存储为‘null’")
    private String picturePath;

    @ApiModelProperty(value = "是否被删除")
    private Integer isDelete;
}
