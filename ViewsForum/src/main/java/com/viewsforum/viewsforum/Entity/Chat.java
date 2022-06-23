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
@Table(name = "chat")
@ApiModel(description = "私聊")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "聊天ID")
    private Integer chatID;

    @ApiModelProperty(value = "发送者ID")
    private Integer sendID;

    @ApiModelProperty(value = "接收者ID")
    private Integer receiveID;

    @ApiModelProperty(value = "私聊内容")
    private String chatContent;

    @ApiModelProperty(value = "发送时间")
    private Timestamp sendTime;

}
