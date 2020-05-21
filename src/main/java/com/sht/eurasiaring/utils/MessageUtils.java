
package com.sht.eurasiaring.utils;

import com.sht.eurasiaring.entity.User;
import lombok.Data;

@Data
public class MessageUtils implements Comparable<MessageUtils> {
	private String images;
	private String name;
	private Integer matterId;
	private String createTime;
	private String content;
	private Integer userId;
	private User user;

	public int compareTo(MessageUtils o) {
		if (getCreateTime().compareTo(o.getCreateTime()) > 0)
			return -1;
		else if (getCreateTime().compareTo(o.getCreateTime()) == 0) {
			return 0;
		}
		return 1;
	}
}
