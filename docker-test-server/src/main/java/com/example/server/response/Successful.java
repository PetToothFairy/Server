package com.example.server.response;

import org.json.JSONObject;

public class Successful {
    private Integer status = 200;
    private String message;
    private JSONObject data;

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setJsonobject(JSONObject data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject getJsonobject() {
        return data;
    }
}
