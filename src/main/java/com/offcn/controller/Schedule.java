package com.offcn.controller;

import com.offcn.bean.Employee;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@EnableScheduling
@RequestMapping("schedule")
public class Schedule {

    @RequestMapping("timeout")
    @Scheduled(fixedRate = 2000)
    public void timeout(){
//        HttpSession session = request.getSession();
//        Object obj = session.getAttribute("employee");
//        System.out.println("定时任务");
//        if (obj!=null || "".equals(obj.toString())){
//            try {
//                response.sendRedirect("timeout.html");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//
//        Session session = SecurityUtils.getSubject().getSession();
//        Employee employee=(Employee) session.getAttribute("employee");
//        if (employee==null || "".equals(employee)){
//            return true;
//        }
//        System.out.println("定时任务");
//        return false;

    }
}
