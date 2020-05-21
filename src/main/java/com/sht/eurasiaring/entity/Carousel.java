package com.sht.eurasiaring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 轮播图
 * @author Hongtao Shen
 * @date 2020/5/16 - 14:14
 **/
@Data
@Entity
@Table(name = "eu_carousel")
public class Carousel implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 轮播图的标题
     */
    private String title;

    /**
     * 每一张轮播图Url
     */
    private String url;
}
