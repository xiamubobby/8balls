package com.knight.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by natsuki on 16/6/16.
 */
@ControllerAdvice
public class IkanExceptionHandler {

    public static class BaseIkanException extends RuntimeException {
        private String tag;
        public BaseIkanException(String tag) {
            this.tag = tag;
        }
    }
    @ResponseStatus(value= HttpStatus.OK)
    public static class UserNotFoundException extends BaseIkanException {
        public UserNotFoundException(String tag) {
            super(tag);
        }
    }

    @ResponseStatus(value= HttpStatus.OK)
    public static class UserNotVipException extends BaseIkanException {
        public UserNotVipException(String tag) {
            super(tag);
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    public static class NoAvailableAccountException extends BaseIkanException {

        public NoAvailableAccountException(String tag) {
            super(tag);
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public Object handleConflict() {
        Map<String, Object> res = new HashMap<>();
        res.put("success",0);
        res.put("message","查无此用户");
        return res;
    }

    @ExceptionHandler(UserNotVipException.class)
    @ResponseBody
    public Object handleNotVip() {
        Map<String, Object> res = new HashMap<>();
        res.put("success",0);
        res.put("message","请先成为魏阿婆.");
        return res;
    }

    @ExceptionHandler(IkanExceptionHandler.NoAvailableAccountException.class)
    @ResponseBody
    public Object handleNoAvailableAccount() {
        Map<String, Object> res = new HashMap<>();
        res.put("success",0);
        res.put("message","暂时没有靠谱的帐号可分配.");
        return res;
    }


}
