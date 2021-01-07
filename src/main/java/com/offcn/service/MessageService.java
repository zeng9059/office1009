package com.offcn.service;

import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Message;
import com.offcn.util.BaseResult;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MessageService {
    /**
     * 发送消息
     * @param message
     * @return
     */
    BaseResult addMessage(Message message);

    /**
     * 根据条件收件人姓名查找发件箱内容
     * @param message
     * @return
     */

    List<Message> findSendMessageByCondition(Message message);

    /**
     * 查询信息的行数
     */
    int getRowsByCondition(Message message);


    /**
     * 根据条件收件人姓名查找发件箱内容
     * @param message
     * @return
     */

    List<Message> findSendMessageByCondition1(Message message);

    /**
     * 查询信息的行数
     */
    int getRowsByCondition1(Message message);

    boolean findMessageDetail(int mid);

    /**
     * 消息详情
     * @return
     */
    Message outboxDetail();

    ResponseEntity<byte[]> downAnnex() throws Exception;
}
