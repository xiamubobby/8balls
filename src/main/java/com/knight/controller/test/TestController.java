package com.knight.controller.test;

/**
 * Created by natsuki on 16/6/13.
 */

import com.knight.entity.account.VideoAccount;
import com.knight.entity.group.AccountGroup;
import com.knight.repository.group.AccountGroupRepository;
import org.hibernate.engine.query.spi.NativeQueryInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    AccountGroupRepository accountGroupRepository;
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @RequestMapping("test1")
    public Map<String, Object> test1() {
        Map<String, Object> res = new HashMap<>();
        res.put("success",1);
        res.put("test1", accountGroupRepository.findAvailableGroupForType(VideoAccount.WebsiteType.IQIYI).get(0).getVideoAccount().toString());
        return res;
    }

    @Transactional
    @RequestMapping("test2")
    public Map<String, Object> test2() {
        Map<String, Object> res = new HashMap<>();
        res.put("success",1);
        res.put("test2", accountGroupRepository.findAvailableGroupForType(VideoAccount.WebsiteType.YOUKU_TUDOU).get(0).getVideoAccount().toString());
        return res;
    }
}
