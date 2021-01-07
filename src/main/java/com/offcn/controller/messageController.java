package com.offcn.controller;

import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Message;
import com.offcn.service.MessageService;
import com.offcn.util.BaseResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("message")
public class messageController {

    /**
     * 发送消息
     */

    @Autowired
    private MessageService messageService;

    @RequestMapping("addMessage")
    @ResponseBody
    public BaseResult addMessage(Message message){

        return messageService.addMessage(message);
    }

    @ResponseBody
    @RequestMapping("findSendMessageByCondition")
    public JSONObject findSendMessageByCondition(Message message){
        JSONObject jsonObject = new JSONObject();
        List<Message> messages = messageService.findSendMessageByCondition(message);
        int total = messageService.getRowsByCondition(message);
        jsonObject.put("rows",messages);
        jsonObject.put("total",total);
        return jsonObject;
    }


    @ResponseBody
    @RequestMapping("findSendMessageByCondition1")
    public JSONObject findSendMessageByCondition1(Message message){
        JSONObject jsonObject = new JSONObject();
        List<Message> messages = messageService.findSendMessageByCondition1(message);
        int total = messageService.getRowsByCondition1(message);
        jsonObject.put("rows",messages);
        jsonObject.put("total",total);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping("findMessageDetail")
    public boolean findMessageDetail(int mid){
        return messageService.findMessageDetail(mid);
    }

    @RequestMapping("outboxDetail")
    @ResponseBody
    public Message outboxDetail(){
        return messageService.outboxDetail();
    }

    @RequestMapping("downAnnex")
    public ResponseEntity<byte[]> downAnnex() throws Exception {
        return messageService.downAnnex();
    }

}
