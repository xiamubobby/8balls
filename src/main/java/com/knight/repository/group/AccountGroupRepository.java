package com.knight.repository.group;

import com.knight.entity.account.VideoAccount;
import com.knight.entity.group.AccountGroup;
import com.knight.entity.user.User;
import com.wonders.xlab.framework.repository.MyRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by natsuki on 16/6/12.
 */
public interface AccountGroupRepository extends MyRepository<AccountGroup, Long> {
    AccountGroup findByAccountId(long accountId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query(nativeQuery = true, value = "SELECT * FROM account_group WHERE (user_one is null or user_two is null or user_three is null or user_four is null or user_five is null) LIMIT 1")
    @Query(value = "SELECT account_group from AccountGroup account_group WHERE account_group.unoccupied != 0")
    List<AccountGroup> findFirstAvailableGroup();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT account_group from AccountGroup account_group WHERE (account_group.unoccupied != 0 and (SELECT video_account.websiteType from VideoAccount video_account WHERE video_account.id = account_group.account.id) = :type)")
    List<AccountGroup> findAvailableGroupForType(@Param("type") VideoAccount.WebsiteType type);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT account_group from AccountGroup account_group WHERE ((account_group.userOne = :user or account_group.userTwo = :user or account_group.userThree = :user or account_group.userFour = :user or account_group.userFive = :user) and (SELECT video_account.websiteType from VideoAccount video_account WHERE video_account.id = account_group.account.id) = :type)")
    List<AccountGroup> findAvailableGroupByUserForType(@Param("user") User user, @Param("type") VideoAccount.WebsiteType type);

    @Query(value = "SELECT account_group from AccountGroup account_group WHERE account_group.usingUser = :usingUser")
    List<AccountGroup> findByUsingUser(@Param("usingUser") User usingUser);

}
