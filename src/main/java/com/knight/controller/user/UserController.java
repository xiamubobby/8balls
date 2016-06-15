package com.knight.controller.user;

import com.knight.entity.account.VideoAccount;
import com.knight.entity.user.User;
import com.knight.entity.user.UserLoginLog;
import com.knight.repository.account.VideoAccountRepository;
import com.knight.repository.user.UserLoginLogRepository;
import com.knight.repository.user.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by knight on 16/04/08.
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserLoginLogRepository userLoginLogRepository;

    @Autowired
    VideoAccountRepository videoAccountRepository;


    /**
     * 注册
     */
    @Transactional
    @RequestMapping("register")
    public Map<String, Object> register(
            @RequestParam String account,
            @RequestParam String password) {

        Map<String, Object> res = new HashMap<>();


        Map<String, Object> filters = new HashMap<>();

        if(account!=null){
            filters.put("account_equal", account);
        }

        if (userRepository.findAll(filters).size()>0) {
            res.put("success",0);
            res.put("message","账户名存在~请换一个试试吧!");
            return res;
        }else{
            User user = new User();
            user.setAccount(account);
            user.setPassword(DigestUtils.md5Hex(password));
            userRepository.save(user);

        }

        res.put("success",1);
        return res;
    }

    /**
     * 登陆
     */
    @RequestMapping("login")
    public Map<String, Object> login(
            HttpServletRequest request,
            @RequestParam String account,
            @RequestParam String password) {

        Map<String, Object> res = new HashMap<>();

        User user = userRepository.findByAccount(account);

        if(user==null){
            res.put("success",0);
            res.put("message","该账号不存在");
            return res;
        }

        user = userRepository.findByAccountAndPasswordAndRemoved(account, DigestUtils.md5Hex(password), false);

        if(user!=null){
            user.setAccessToken(UUID.randomUUID().toString());
            user.setAccessTime(new Date().getTime() + (long) 365 * 24 * 60 * 60 * 1000);
            userRepository.save(user);
            res.put("accessToken", user.getAccessToken());
            res.put("name", user.getAccount());

            UserLoginLog userLoginLog = new UserLoginLog();
            userLoginLog.setUser(user);
            userLoginLog.setIpAddress(request.getRemoteAddr());
            userLoginLogRepository.save(userLoginLog);

        }else{
            res.put("success", 0);
            res.put("message", "账号密码错误");
            return res;
        }

        res.put("success",1);
        return res;
    }


    /**
     * 登出
     */
    @RequestMapping("logout")
    public Map<String ,Object> logout(
            @RequestHeader String accessToken){

        Map<String, Object> res = new HashMap<>();

        User user = userRepository.findByAccessToken(accessToken);

        if(user!=null){
            user.setAccessToken("");
            user.setAccessTime(0);
            userRepository.save(user);
        }

        res.put("success",1);
        return res;
    }

    /**
     * 登陆
     */
    @RequestMapping("iklogin")
    public Map<String ,Object> iklogin(
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String password){

        Map<String, Object> res = new HashMap<>();

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");

        res.put("success",1);
        res.put("account",  sdf.format(now)+"@qq.com");
        res.put("password", "12345678");

//        res.put("remoteUser", request.getRemoteUser());
//        res.put("remoteAddr", request.getRemoteAddr());
//        Enumeration<String> enumeration = request.getHeaderNames();
//        while(enumeration.hasMoreElements()){
//            res.put(enumeration.nextElement(), request.getHeader(enumeration.nextElement()));
//        }
//        res.put("x-header", request.getHeader("x-forwarded-for"));
//        res.put("pc-header", request.getHeader("Proxy-Client-IP"));
//        res.put("wpc-header", request.getHeader("WL-Proxy-Client-IP"));


//        if (request.getHeader("x-forwarded-for") == null) {
//            System.out.println("remoteAddr : " + request.getRemoteAddr());
//        }else{
//            System.out.println("x-header : " + request.getHeader("x-forwarded-for"));
//        }



        return res;
    }

    @RequestMapping("getIkanAccount")
    public Map<String ,Object> getIkanAccount(
            @RequestParam int website,
            @RequestHeader String accessToken){

        Map<String, Object> res = new HashMap<>();


        User user = userRepository.findByAccessToken(accessToken);

        if(user!=null){
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
            List<VideoAccount> videoAccounts = videoAccountRepository.findByWebsiteType(VideoAccount.WebsiteType.values()[website]);

            if(videoAccounts.size()>0){
                VideoAccount videoAccount =  videoAccounts.get(0);
                res.put("success",1);
                res.put("account",videoAccount.getAccount());
                res.put("password",videoAccount.getPassword());
                res.put("websiteType",videoAccount.getWebsiteType().ordinal());
            }
        }else{
            res.put("success",0);
            res.put("message","请重新登陆!");
            return res;
        }

        return res;
    }



}

