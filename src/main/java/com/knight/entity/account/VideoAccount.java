package com.knight.entity.account;

import com.wonders.xlab.framework.entity.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by knight on 16/4/11.
 */
@Entity
@Table
public class VideoAccount extends AbstractBaseEntity<Long> {

    private String account;

    private String password;

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
}
