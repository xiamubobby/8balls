package com.knight.repository.user;

import com.knight.entity.user.User;
import com.wonders.xlab.framework.repository.MyRepository;

/**
 * Created by knight on 16/04/08.
 */
public interface UserRepository extends MyRepository<User, Long> {

    User findByAccount(String account);

    User findByAccountAndPasswordAndRemoved(String account,String password,boolean isRemoved);

    User findByAccessToken(String accessToken);

}
