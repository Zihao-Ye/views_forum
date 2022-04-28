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
@Table(name = "picture")
@ApiModel(description = "图片存储")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "图片ID")
    private Integer pictureID;

    @ApiModelProperty(value = "图片储存位置")
    private String link;

    @ApiModelProperty(value = "图片所属帖子/评论ID")
    private Integer belongID;

    @ApiModelProperty(value = "图片种类", notes = "1：帖子附带的图片 2：评论附带的图片")
    private Integer pictureType;
}
