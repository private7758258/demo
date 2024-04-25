package com.ioexample.controller;

import com.ioexample.utils.ReqContextUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
    * @description: 消息推送(订阅功能),demo页面在static下
 */
@CrossOrigin
@RestController
public class SubscribeController {

    @RequestMapping("/subscribe")
    public void subscribe(HttpServletRequest req, HttpServletResponse res, @RequestParam("topic") String topic) {
        ReqContextUtils.addSubscrib(topic, req, res);
    }

    @RequestMapping("/publish")
    public void publish(@RequestParam("topic") String topic, @RequestParam("content") String content) {
        ReqContextUtils.publishMessage(topic, content);
    }

}
