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
@Table(name = "system_message")
@ApiModel(description = "系统消息")
public class SystemMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "系统消息ID")
    private Integer systemMessageID;

    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @ApiModelProperty(value = "接收者ID")
    private Integer userID;

    @ApiModelProperty(value = "消息种类", notes = "1：申请信息（创建者接收申请信息、申请者接收到拒绝/同意/被解除管理员信息）2：回复信息（帖子创建者接收到发言信息、发言/回复接收到回复信息） 3：关注信息（被关注信息） 4：警告/删除信息（主题被删除、帖子被删除、发言被删除、回复被删除）")
    private Integer messageType;

    @ApiModelProperty(value = "是否已读")
    private Integer isRead;

    @ApiModelProperty(value = "发送时间")
    private Timestamp messageTime;
}
