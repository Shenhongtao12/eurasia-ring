package com.sht.eurasiaring.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User用户信息
 * @author Hongtao Shen
 * @date 2020/5/16 - 12:16
 **/
@JsonInclude(JsonInclude.Include.NON_NULL) //返回信息忽略空值
@Data
@Table(name = "eu_user")
@Entity
public class User implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //设置自增策略
    private Integer id;

    /**
     *用户名
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密匙
     */
    @Transient
    private String session_key;

    /**
     * 新用户首次登陆时间
     */
    private String createTime;

    /**
     * 登录code
     */
    @Transient
    private String js_code;

    /**
     * 登录错误时的状态码
     */
    @Transient
    private Integer errcode;

    @Transient
    private Integer flag;

}
