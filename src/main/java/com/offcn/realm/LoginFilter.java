//package com.offcn.realm;
//
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.web.filter.AccessControlFilter;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//
//public class LoginFilter extends AccessControlFilter {
//    @Override
//    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
//        SecurityUtils.getSubject().getPrincipal();
//        return false;
//    }
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
//        return false;
//    }
//}
