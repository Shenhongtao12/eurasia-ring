package com.sht.eurasiaring.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel("返回结果实体类")
public class JsonData implements Serializable {

    private static final Long serialVersionUID = 1L;
    @ApiModelProperty("响应码：0 成功 -1 失败")
    private Integer code;
    @ApiModelProperty("响应的数据")
    private Object data;
    @ApiModelProperty("响应提示信息")
    private String msg;

    public JsonData() {
    }

    public JsonData(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static JsonData buildSuccess() {
        return new JsonData(0, null, null);
    }

    public static JsonData buildError(Object data, String msg) {
        return new JsonData(-1, data, msg);
    }

    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    public static JsonData buildError(String msg, Integer code) {
        return new JsonData(code, null, msg);
    }

    public static JsonData buildSuccess(Object data, String msg) {
        return new JsonData(0, data, msg);
    }

    public static JsonData buildSuccess(String msg) {
        return new JsonData(0, null, msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "JsonData [code=" + this.code + ", data=" + this.data + ", msg=" + this.msg + "]";
    }
}
