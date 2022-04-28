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
@Table(name = "black")
@ApiModel(description = "黑名单")
public class Black {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "黑名单ID")
    private Integer blackID;

    @ApiModelProperty(value = "拉黑者ID")
    private Integer blackerID;

    @ApiModelProperty(value = "被拉黑者ID")
    private Integer blackedID;

    @ApiModelProperty(value = "拉黑时间")
    private Timestamp blackedTime;
}
