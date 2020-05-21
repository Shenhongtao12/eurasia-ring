package com.sht.eurasiaring.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 给留言的回复
 */
@Data
@Entity
@Table(name = "eu_reply")
@DynamicUpdate
public class Reply implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String createTime;
	private String content;
	private Integer number;
	private Integer leaf;   //等于0时就是一个树的末尾
	private Integer userId;  //发布回复的人id
	private Integer matterId;  //事件的id
	private Integer commentId;
	private Integer nameId;  //给谁回复的人id 父id
	private Integer parentId; // reply的父id

	@Transient
	private String parentName;//父name
	@Transient
	private Object state;  //判断点赞
	@Transient
	private User user;
	@Transient
	private List<Reply> replyList;


}
