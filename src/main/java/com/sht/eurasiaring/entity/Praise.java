package com.sht.eurasiaring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Hongtao Shen
 * @date 2020/5/24 - 19:58
 **/
@Table(name = "eu_praise")
@Entity
@Data
public class Praise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;
    private Integer typeId;
    private Integer typeUserId;
    private Integer userId;
    private String createTime;

    @Transient
    private User user;
}
