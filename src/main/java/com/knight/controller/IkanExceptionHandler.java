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
    @ResponseStatus(value= HttpStatus.OK)
    public static class UserNotFoundException extends RuntimeException {
        private String tag;
        public UserNotFoundException(String tag) {}
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public Object handleConflict() {
        Map<String, Object> res = new HashMap<>();
        res.put("success",0);
        res.put("message","查无此用户");
        return res;
    }
}
