package com.offcn.dao;

import com.offcn.bean.Message;
import com.offcn.bean.MessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    long countByExample(MessageExample example);

    int deleteByExample(MessageExample example);

    int deleteByPrimaryKey(Integer mid);

    int insert(Message record);

    int insertSelective(Message record);

    List<Message> selectByExample(MessageExample example);

    Message selectByPrimaryKey(Integer mid);

    int updateByExampleSelective(@Param("record") Message record, @Param("example") MessageExample example);

    int updateByExample(@Param("record") Message record, @Param("example") MessageExample example);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    /**
     * 条件查询发件箱信息
     * @param message
     * @return
     */
    List<Message> findSendMessageByCondition(Message message);

    /**
     * 条件查询消息条数
     * @param message
     * @return
     */
    int getRowsByCondition(Message message);

    /**
     * 条件查询收件箱信息
     * @param message
     * @return
     */
    List<Message> findSendMessageByCondition1(Message message);

    /**
     * 条件查询消息条数
     * @param message
     * @return
     */
    int getRowsByCondition1(Message message);
}