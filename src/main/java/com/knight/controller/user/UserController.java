package com.knight.controller.user;

import com.knight.controller.IkanExceptionHandler;
import com.knight.dto.responsebody.UserInfo;
import com.knight.entity.account.RandomAccount;
import com.knight.entity.account.VideoAccount;
import com.knight.entity.group.AccountGroup;
import com.knight.entity.user.User;
import com.knight.entity.user.UserLoginLog;
import com.knight.repository.account.RandomAccountRepository;
import com.knight.repository.account.VideoAccountRepository;
import com.knight.repository.group.AccountGroupRepository;
import com.knight.repository.user.UserLoginLogRepository;
import com.knight.repository.user.UserRepository;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    AccountGroupRepository accountGroupRepository;

    @Autowired
    RandomAccountRepository randomAccountRepository;

    static VideoAccount.WebsiteType[] types = VideoAccount.WebsiteType.values();

    static RandomAccount.WebsiteType[] randomTypes = RandomAccount.WebsiteType.values();


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
            user = new User();
            user.setAccount(account);
            user.setPassword(password);
            userRepository.save(user);
//            res.put("success",0);
//            res.put("message","该账号不存在");
//            return res;
        }

        user = userRepository.findByAccountAndPasswordAndRemoved(account, password, false);

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

        if(user != null){
            clearAccountUseState(user);
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

    @Transactional
    @RequestMapping("getIkanAccount")
    public Map<String ,Object> getIkanAccount(
            @RequestParam int website,
            @RequestHeader String accessToken){

        Map<String, Object> res = new HashMap<>();

        if (accessToken.isEmpty()) {
            res.put("success", 1);
            return res;
        }

        User user = userRepository.findByAccessToken(accessToken);

        if (user == null) throw new IkanExceptionHandler.UserNotFoundException(accessToken);
        if (!user.isVip()) throw new IkanExceptionHandler.UserNotVipException(accessToken);

        VideoAccount videoAccount = null;

        clearAccountUseState(user);

        boolean useRandom = false;
//        Date now = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        List<AccountGroup> distributed =  accountGroupRepository.findAvailableGroupByUserForType(user, types[website]);

        if (distributed.size() == 0) {
            List<AccountGroup> candidates = accountGroupRepository.findAvailableGroupForType(types[website]);
            AccountGroup candidate = null;
            if (candidates.size() > 0) {
                candidate = candidates.get(0);
                switch (candidate.getUnoccupied()) {
                    case 5:
                        candidate.setUserOne(user);
                        break;
                    case 4:
                        candidate.setUserTwo(user);
                        break;
                    case 3:
                        candidate.setUserThree(user);
                        break;
                    case 2:
                        candidate.setUserFour(user);
                        break;
                    case 1:
                        candidate.setUserFive(user);
                        break;
                }
                candidate.setUnoccupied(candidate.getUnoccupied() - 1);
                accountGroupRepository.save(candidate);
                distributed.add(candidate);
            } else {
                useRandom = true;
            }

        } else {
            if (distributed.get(0).getUsingUser() != null && distributed.get(0).getUsingUser() != user) {
                useRandom = true;
            } else {
                try {
                    videoAccount = distributed.get(0).getVideoAccount();
                    distributed.get(0).setUsingUser(user);
                    accountGroupRepository.save(distributed.get(0));
                } catch (Exception ignored) {}
            }
        }

        if (useRandom) {
            List<RandomAccount> randomAccounts = randomAccountRepository.findByWebsiteTypeAndUsed(randomTypes[website], false);
            RandomAccount randomAccount = null;
            if (randomAccounts.size() > 0) {
                randomAccount = randomAccounts.get(0);
            } else throw new IkanExceptionHandler.NoAvailableAccountException(accessToken);
            if (randomAccount != null) {
                videoAccount = new VideoAccount();
                videoAccount.setAccount(randomAccount.getAccount());
                videoAccount.setPassword(randomAccount.getPassword());
                videoAccount.setWebsiteType(types[randomAccount.getWebsiteType().ordinal()]);
                randomAccount.setUsed(true);
                randomAccount.setUsing(user);
                randomAccountRepository.save(randomAccount);
            }
        }

        if(videoAccount != null) {
            res.put("success",1);
            res.put("account",videoAccount.getAccount());
            res.put("password",videoAccount.getPassword());
            res.put("websiteType",videoAccount.getWebsiteType().ordinal());
        } else {
            res.put("success",0);
            res.put("message","暂时没有合适的帐号, 请稍候. ");
        }

        return res;
    }

    @RequestMapping("switchVip")
    public Map<String, Object> switchVip(@RequestHeader("accessToken") String accessToken) {
        Map<String, Object> res = new HashMap<>();
        User user = userRepository.findByAccessToken(accessToken);
        if (user != null) {
            if (user.isVip()) {
                user.setVip(false);
                res.put("nowStatus", false);
            } else {
                user.setVip(true);
                res.put("nowStatus", true);
            }
        } else {
            throw new IkanExceptionHandler.UserNotFoundException(accessToken);
        }
        userRepository.save(user);
        res.put("success", 1);
        return res;
    }

    @RequestMapping(value = "getUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "ttt", response = UserInfo.class, responseContainer = "List")
    public @ResponseBody UserInfo getUserInfo(@RequestHeader("accessToken") String accessToken) {
        UserInfo ret = new UserInfo();
        User user =  userRepository.findByAccessToken(accessToken);
        if (user == null) throw new IkanExceptionHandler.UserNotFoundException(accessToken);
        ret.setSuccess(1);
        ret.setVip(user.isVip());
        return ret;
    }

    private boolean logoutImp(String token) {
        User toBeLoggedOut = userRepository.findByAccessToken(token);
        if (toBeLoggedOut != null) {
            toBeLoggedOut.setAccessToken(null);
            userRepository.save(toBeLoggedOut);
            return true;
        } else {
            return false;
        }
    }

    private void clearAccountUseState(User user) {
        List<AccountGroup> accountGroups = accountGroupRepository.findByUsingUser(user);
        List<RandomAccount> randomAccounts = randomAccountRepository.findByUsing(user);
        for (AccountGroup accountGroup : accountGroupRepository.findByUsingUser(user)) {
            accountGroup.setUsingUser(null);
            accountGroupRepository.save(accountGroup);
        }
        for (RandomAccount randomAccount : randomAccountRepository.findByUsing(user)) {
            randomAccount.setUsed(false);
            randomAccount.setUsing(null);
            randomAccountRepository.save(randomAccount);
        }
    }



}

