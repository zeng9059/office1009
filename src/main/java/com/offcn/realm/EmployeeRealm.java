package com.offcn.realm;

import com.offcn.bean.Employee;
import com.offcn.bean.EmployeeExample;
import com.offcn.bean.Function;
import com.offcn.bean.Role;
import com.offcn.dao.EmployeeMapper;
import com.offcn.dao.FunctionMapper;
import com.offcn.dao.RoleMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmployeeRealm extends AuthorizingRealm {
    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RoleMapper roleMapper;


    /**
     * 获取授权信息的方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        //获取当前登录的用户
        Employee employee=(Employee) principalCollection.getPrimaryPrincipal();
        //获取当前用户的角色信息
        List<Role> roles = roleMapper.findRolesByEid(employee.getEid());
        for (Role role:roles){
            simpleAuthorizationInfo.addRole(role.getRcode());
            System.out.println(role.getRcode());
        }

        //获取当前用户的功能信息
        List<Function> functions = functionMapper.getCurrentFunctions(employee.getEid());
        for(Function function:functions){
            simpleAuthorizationInfo.addStringPermission(function.getFcode());

        }


        return simpleAuthorizationInfo;
    }

    /**
     * 获取认证信息的方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户名和密码
        UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
        //根据工号查询员工信息
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andJobNumberEqualTo(token.getUsername());
        List<Employee> employees = employeeMapper.selectByExample(example);
       if (employees!=null&&employees.size()>0){
           Employee employee=employees.get(0);
           SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(employee, employee.getPassword(), token.getUsername());
           //设置认证盐
           info.setCredentialsSalt(ByteSource.Util.bytes(employee.getRemark1()));
           return info;
       }

        return null;
    }
}
