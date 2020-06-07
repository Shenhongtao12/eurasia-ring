package com.sht.eurasiaring.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "eu_fans")
@ApiModel("关注实体类")
public class Fans implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String createTime;
	@ApiModelProperty("被关注的人的id")
	private Integer userId;
	@ApiModelProperty("粉丝的id")
	private Integer fansId;
	@Transient
	private User user;

}
