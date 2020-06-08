package com.sht.eurasiaring.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Hongtao Shen
 * @date 2020/6/8 - 20:39
 **/
@Data
@Entity
@Table(name = "eu_complaint")
@ApiModel("投诉实体类")
public class Complaint implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 投诉内容
     */
    @ApiModelProperty(value = "投诉内容",required = true)
    private String content;

    /**
     * 证据图片
     */
    @ApiModelProperty(value = "图片证据", required = true)
    private String evidenceImages;

    private String createTime;

    /**
     * 投诉状态后台处理，0：刚提交，1：正在处理，2：处理完成
     */
    @ApiModelProperty(value = "投诉状态后台处理，0：刚提交，1：正在处理，2：处理完成")
    private Integer status;

    private Integer userId;
}
