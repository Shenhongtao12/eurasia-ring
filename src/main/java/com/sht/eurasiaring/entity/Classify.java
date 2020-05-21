package com.sht.eurasiaring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 17:26
 **/
@Data
@Table(name = "eu_classify")
@Entity
public class Classify implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //设置自增策略
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类图片Url
     */
    private String classifyUrl;
}
