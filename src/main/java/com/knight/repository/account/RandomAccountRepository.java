package com.knight.repository.account;

import com.knight.entity.account.RandomAccount;
import com.knight.entity.user.User;
import com.wonders.xlab.framework.repository.MyRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by natsuki on 16/6/16.
 */
public interface RandomAccountRepository extends MyRepository<RandomAccount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<RandomAccount> findByWebsiteTypeAndUsed(RandomAccount.WebsiteType websiteType, boolean used);

    List<RandomAccount> findByUsing(User usingUser);
}
