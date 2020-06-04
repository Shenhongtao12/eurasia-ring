package com.sht.eurasiaring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 帖子
 * @author Hongtao Shen
 * @date 2020/5/17 - 18:42
 **/
@Data
@Entity
@Table(name = "eu_post")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    @Column(name = "images_url")
    private String imagesUrl;
    private String createTime;
    private Integer userId;
    private Integer matterId;
    private Integer classifyId;
    private Integer views;
    /**
     * 点赞数量
     */
    @Column(name = "number")
    private Integer number;
    /**
     * 留言的数量
     */
    @Transient
    private Integer commentNum;
    /**
     * 判断是否点赞
     */
    @Transient
    private String status;
    @Transient
    private User user;
    @Transient
    private List<Comment> commentList;
}
