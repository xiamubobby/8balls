package com.knight.controller.test;

/**
 * Created by natsuki on 16/6/13.
 */

import com.knight.entity.account.RandomAccount;
import com.knight.entity.account.VideoAccount;
import com.knight.entity.group.AccountGroup;
import com.knight.entity.user.User;
import com.knight.repository.account.RandomAccountRepository;
import com.knight.repository.account.VideoAccountRepository;
import com.knight.repository.group.AccountGroupRepository;
import com.knight.repository.user.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.engine.query.spi.NativeQueryInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    AccountGroupRepository accountGroupRepository;
    @Autowired
    RandomAccountRepository randomAccountRepository;
    @Autowired
    VideoAccountRepository videoAccountRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    @RequestMapping("test1")
    public Map<String, Object> test1() {
        VideoAccount.WebsiteType[] ss = VideoAccount.WebsiteType.values();
        System.out.println(ss.length);
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
//            RandomAccount ac = new RandomAccount();
//            ac.setAccount("r"+i);
//            ac.setPassword("rp"+i);
//            int ni = r.nextInt(5);
//            System.out.print(ni);
//            RandomAccount.WebsiteType nwt = ss[ni];
//            System.out.print(nwt.name());
//            ac.setWebsiteType(nwt);
//            randomAccountRepository.save(ac);

//            VideoAccount ac = new VideoAccount();
//            ac.setAccount("f"+i);
//            ac.setPassword("fp"+i);
//            int ni = r.nextInt(5);
//            System.out.print(ni);
//            VideoAccount.WebsiteType nwt = ss[ni];
//            System.out.println(nwt.name());
//            ac.setWebsiteType(nwt);
//            videoAccountRepository.save(ac);
            User user = new User();
            user.setAccount("u"+i);
            user.setPassword(DigestUtils.md5Hex("up"+i));
            userRepository.save(user);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("success",1);
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
