package com.knight.entity.group;

import com.knight.entity.account.VideoAccount;
import com.knight.entity.user.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * Created by natsuki on 16/6/12.
 */
@Entity
@Table
public class AccountGroup extends AbstractPersistable<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private VideoAccount account;
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "user_one")
    private User userOne;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_two")
    private User userTwo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_three")
    private User userThree;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_four")
    private User userFour;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_five")
    private User userFive;
    private int unoccupied;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "using_user")
    private User usingUser;


    public int getUnoccupied() {
        return unoccupied;
    }

    public void setUnoccupied(int unoccupied) {
        this.unoccupied = unoccupied;
    }

    public VideoAccount getAccount() {
        return account;
    }

    public void setAccount(VideoAccount account) {
        this.account = account;
    }

    public VideoAccount getVideoAccount() {
        return account;
    }

    public void setVideoAccount(VideoAccount videoAccount) {
        this.account = videoAccount;
    }

    public User getUserOne() {
        return userOne;
    }

    public void setUserOne(User userOne) {
        this.userOne = userOne;
    }

    public User getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(User userTwo) {
        this.userTwo = userTwo;
    }

    public User getUserThree() {
        return userThree;
    }

    public void setUserThree(User userThree) {
        this.userThree = userThree;
    }

    public User getUserFour() {
        return userFour;
    }

    public void setUserFour(User userFour) {
        this.userFour = userFour;
    }

    public User getUserFive() {
        return userFive;
    }

    public void setUserFive(User userFive) {
        this.userFive = userFive;
    }

    public User getUsingUser() {
        return usingUser;
    }

    public void setUsingUser(User usingUser) {
        this.usingUser = usingUser;
    }
}
