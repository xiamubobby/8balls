package com.knight.entity.user;

import com.wonders.xlab.framework.entity.AbstractBaseEntity;

import javax.persistence.*;

/**
 * 登陆日志 记录ip
 * Created by knight on 16/4/8.
 */
@Entity
@Table
public class UserLoginLog extends AbstractBaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String ipAddress;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
