package com.dovepay.doveEditor.response;

import com.alibaba.fastjson.annotation.JSONField;

public class ErrorResponse {
    @JSONField(ordinal = 1)
    private Integer code;
    @JSONField(ordinal = 2)
    private String name;
    @JSONField(ordinal = 3)
    private String message;
    public ErrorResponse(Integer code, String name, String message) {
        this.code = code;
        this.name = name;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
