package com.knight.repository.account;

import com.knight.entity.account.VideoAccount;
import com.wonders.xlab.framework.repository.MyRepository;

import java.util.List;

/**
 * Created by knight on 16/4/11.
 */
public interface VideoAccountRepository extends MyRepository<VideoAccount, Long> {

    List<VideoAccount> findByWebsiteType(VideoAccount.WebsiteType websiteType);

}
