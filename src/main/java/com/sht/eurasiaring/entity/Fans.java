package com.sht.eurasiaring.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "eu_fans")
public class Fans implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String createTime;
	private Integer userId;
	private Integer fansId;
	@Transient
	private User user;

}
