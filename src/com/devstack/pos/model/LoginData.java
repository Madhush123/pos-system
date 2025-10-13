package com.devstack.pos.model;

public class LoginData {
    int statusCode;
    boolean status;
    String msg;
    String email;
    String displayName;

    public LoginData() {
    }

    public LoginData(int statusCode, boolean status, String msg, String email, String displayName) {
        this.statusCode = statusCode;
        this.status = status;
        this.msg = msg;
        this.email = email;
        this.displayName = displayName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
