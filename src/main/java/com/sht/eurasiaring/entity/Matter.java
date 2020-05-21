package com.sht.eurasiaring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 发生的事件
 * @author Hongtao Shen
 * @date 2020/5/17 - 18:42
 **/
@Data
@Entity
@Table(name = "eu_matter")
public class Matter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private String imagesUrl;
    private String createTime;
    private Integer userId;

    @Transient
    private List<Comment> commentList;
}
