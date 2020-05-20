package com.sht.eurasiaring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 留言
 */
@Data
@Table(name = "eu_comment")
@Entity
public class Comment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	private String content;
	private String createTime;
	private Integer number;
	private Long userId;  //留言的发布人id
	private Long matterId;
	private Integer leaf;  //null 用来区分留言和回复

	@Transient
	private Object state;  //判断点赞
	@Transient
	private User user;
	@Transient
	private List<Reply> replyList;

}
