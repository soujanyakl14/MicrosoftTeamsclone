package com.example.microsoftclone.model;

import java.io.Serializable;

public class message implements Serializable {

    public String id,msg;

    public message(String msg,String id) {
        this.msg = msg;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
