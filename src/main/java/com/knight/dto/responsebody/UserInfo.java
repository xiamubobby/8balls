package com.knight.dto.responsebody;

/**
 * Created by natsuki on 16/6/17.
 */
public class UserInfo extends BaseResponseBody {
    boolean isVip;

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}
