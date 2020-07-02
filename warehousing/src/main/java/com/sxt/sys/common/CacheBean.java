package com.sxt.sys.common;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CacheBean {

    private String key;

    private Object value;

    public CacheBean(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public CacheBean() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return JSON.toJSON(value).toString();
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
