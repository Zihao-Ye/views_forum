package com.viewsforum.viewsforum.Pojo.PostControllerPojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel(description = "评论信息")
public class DetailComment {
    @ApiModelProperty(value = "评论ID")
    private Integer commentID;

    @ApiModelProperty(value = "评论者ID")
    private Integer createID;

    @ApiModelProperty(value = "评论者名字")
    private String createName;

    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    @ApiModelProperty(value = "评论时间")
    private Timestamp commentTime;

    @ApiModelProperty(value = "评论下回复数")
    private Integer reviewNum;

    @ApiModelProperty(value = "图片路径")
    private String picturePath;

    @ApiModelProperty(value = "回复列表")
    private List<DetailReview> detailReviewList;

    public DetailComment(){
        this.detailReviewList=new ArrayList<DetailReview>();
    }
}
