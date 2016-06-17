package com.knight.entity.account;

import com.knight.entity.user.User;
import com.wonders.xlab.framework.entity.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by natsuki on 16/6/16.
 */
@Entity
@Table
public class RandomAccount extends AbstractBaseEntity<Long> {

    private String account;

    private String password;

    private boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    private User using;

    public enum WebsiteType{
        YOUKU_TUDOU,IQIYI,QQ,SOHU,LE
    }

    private WebsiteType websiteType;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WebsiteType getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(WebsiteType websiteType) {
        this.websiteType = websiteType;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public User getUsing() {
        return using;
    }

    public void setUsing(User using) {
        this.using = using;
    }
}
