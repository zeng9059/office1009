package com.offcn.service;

import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Employee;
import com.offcn.bean.Message;
import com.offcn.bean.MessageExample;
import com.offcn.dao.MessageMapper;
import com.offcn.util.BaseResult;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {


    @Autowired
    private  MessageMapper messageMapper;
    Logger logger=Logger.getLogger(MessageServiceImpl.class);


    @Override
    public BaseResult addMessage(Message message) {
        BaseResult result = new BaseResult();
        message.setMdate(new Date());
        logger.info("发送消息开始，入参："+message);

        try {
            //获取发件人
            Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("employee");
            message.setSender(employee.getEname());
            //获取文件名
            MultipartFile mfile = message.getMfile();
            String annex= UUID.randomUUID().toString()+mfile.getOriginalFilename();


            message.setAnnex(annex);
            //定义文件上传路径
            File filePath=new File("src/main/webapp/mfile/"+annex);
            //文件上传
            try {
                mfile.transferTo(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                result.setMessage("发送附件失败");
                result.setSuccess(false);
                return result;
            }
            message.setMstatu(0);
            //发送消息
            messageMapper.insert(message);
            result.setSuccess(true);
            result.setMessage("发送成功");

        }catch (Exception e){
            e.printStackTrace();
            result.setMessage("发送失败");
            result.setSuccess(false);
            return result;

        }
        logger.info("发送消息结束。出参："+result);
        return result;

    }

    @Override
    public List<Message> findSendMessageByCondition(Message message) {
        Employee employee=(Employee) SecurityUtils.getSubject().getSession().getAttribute("employee");
        message.setSender(employee.getEname());
        message.setLimitStart((message.getPage()-1)*message.getRows());
        return messageMapper.findSendMessageByCondition(message);
    }

    @Override
    public int getRowsByCondition(Message message) {
        return messageMapper.getRowsByCondition(message);
    }

    @Override
    public List<Message> findSendMessageByCondition1(Message message) {
        logger.info("查询收件人，入参："+message);
        Employee employee=(Employee) SecurityUtils.getSubject().getSession().getAttribute("employee");
        message.setReciver(employee.getEname());
        message.setLimitStart((message.getPage()-1)*message.getRows());
        List<Message> messages = messageMapper.findSendMessageByCondition1(message);
        for(Message message1:messages){
            Date date=message1.getMdate();
            String strDateFormat = "yyyy年MM月dd日 HH:mm:ss";
            SimpleDateFormat sdf=new SimpleDateFormat(strDateFormat);
            String time = sdf.format(date);
            message1.setMdateStr(time);
        }
        return messages;
    }

    @Override
    public int getRowsByCondition1(Message message) {
        return messageMapper.getRowsByCondition1(message);
    }

    @Override
    public boolean findMessageDetail(int mid) {
        Message message = messageMapper.selectByPrimaryKey(mid);
        message.setRemark1("1");
        messageMapper.updateByPrimaryKeySelective(message);
        SecurityUtils.getSubject().getSession().setAttribute("message",message);
        return true;
    }

    @Override
    public Message outboxDetail() {
        return (Message)SecurityUtils.getSubject().getSession().getAttribute("message");
    }

    @Override
    public ResponseEntity<byte[]> downAnnex() throws Exception{
        //获取当前要下载的文件名
        Message message = (Message) SecurityUtils.getSubject().getSession().getAttribute("message");
        String annex=message.getAnnex();
        File file=new File("src/main/webapp/mfile/"+annex);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment",annex);
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file),httpHeaders, HttpStatus.OK);

    }
}
