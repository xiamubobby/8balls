package com.knight.dto;

/**
 * Created by natsuki on 16/6/17.
 */
public class UserInfo {
    int success;
    boolean isVip;

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
