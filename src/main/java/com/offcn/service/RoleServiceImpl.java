package com.offcn.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.offcn.bean.*;
import com.offcn.dao.EmployeeRoleMapper;
import com.offcn.dao.RoleFunctionMapper;
import com.offcn.dao.RoleMapper;
import com.offcn.util.BaseResult;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private EmployeeRoleMapper employeeRoleMapper;



    Logger logger=Logger.getLogger(RoleServiceImpl.class);
    @Autowired
    private RoleFunctionMapper roleFunctionMapper;

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public List<Role> findRolesByCondition(Role role) {
        role.setLimitStart((role.getPage()-1)*role.getRows());
        List<Role> roles = roleMapper.findRolesByCondition(role);

        return roles;
    }

    @Override
    public int countRowsByCondition(Role role) {
        return  roleMapper.countRowsByCondition(role);
    }

    @Override
    @Transactional
    public BaseResult addRole(Role role) {
        logger.info("新增角色入参："+role);
        BaseResult result = new BaseResult();

        try{

            //校验角色编码不能重复
            RoleExample roleExample = new RoleExample();
            RoleExample.Criteria criteria = roleExample.createCriteria();
            criteria.andRcodeEqualTo(role.getRcode());
            List<Role> roles = roleMapper.selectByExample(roleExample);
            if (roles!=null&&roles.size()>0){
                result.setSuccess(false);
                result.setMessage("角色重复");
                return result;
            }
            System.out.println("+++++++++======================================");
            System.out.println(role.getRid());

            //新增角色
            int insert = roleMapper.insert(role);

            System.out.println("++++++++++++++++=================================");
            System.out.println(role.getRid());

            //新增角色功能关系表
            String[] fidArray=role.getFids().split(",");
            for (String fid:fidArray){
                RoleFunction roleFunction=new RoleFunction();
                roleFunction.setRid(role.getRid());
                roleFunction.setFid(Integer.parseInt(fid));
                roleFunctionMapper.insert(roleFunction);
            }
            result.setMessage("操作成功");
            result.setSuccess(true);

        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("操作失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();


        }
        logger.info("新增角色，出参："+result);
        return result;

    }

    @Override
    public BaseResult getDetailRole(int rid) {
        BaseResult result = new BaseResult();
        Role role = roleMapper.selectByPrimaryKey(rid);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("role",role);
        System.out.println(role);
        result.setSuccess(true);
        return result;
    }

    @Override
    public Role getRoleFunctionDetaile(int rid) {

        return  roleFunctionMapper.getRoleFunctionDetaile(rid);
    }
    @Transactional
    @Override
    public BaseResult updateRole(Role role) {
        logger.info("进入修改角色方法，入参："+role);
        BaseResult baseResult = new BaseResult();
        try{
            //校验角色编码唯一
            RoleExample example = new RoleExample();
            RoleExample.Criteria criteria = example.createCriteria();
            criteria.andRcodeEqualTo(role.getRcode());
            criteria.andRidNotEqualTo(role.getRid());
            List<Role> roles = roleMapper.selectByExample(example);
            if (roles!=null&&roles.size()>0){
                baseResult.setSuccess(false);
                baseResult.setMessage("角色编码重复");
                return baseResult;
            }
            //修改角色基本信息
            roleMapper.updateByPrimaryKey(role);
            //删除角色功能关系表以前的数据
            RoleFunctionExample example1 = new RoleFunctionExample();
            RoleFunctionExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andRidEqualTo(role.getRid());
            roleFunctionMapper.deleteByExample(example1);
            //新增角色功能关系表数据
            String[] fidArray=role.getFids().split(",");
            for (String fid:fidArray){
                RoleFunction roleFunction = new RoleFunction();
                roleFunction.setRid(role.getRid());
                roleFunction.setFid(Integer.parseInt(fid));
                roleFunctionMapper.insert(roleFunction);
            }

            baseResult.setMessage("操作成功");
            baseResult.setSuccess(true);

        }catch (Exception e){
            e.printStackTrace();
            baseResult.setSuccess(false);
            baseResult.setMessage("操作失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }

        logger.info("修改角色功能结束，出参："+baseResult);
        return baseResult;
    }

    @Transactional
    @Override
    public BaseResult deleteRoleByRid(int rid) {
        logger.info("删除角色信息方法开始：入参"+rid);
        BaseResult result=new BaseResult();
        try {
            //删除角色功能表
            RoleFunctionExample example = new RoleFunctionExample();
            RoleFunctionExample.Criteria criteria = example.createCriteria();
            criteria.andRidEqualTo(rid);
            roleFunctionMapper.deleteByExample(example);
            //删除角色用户关系表数据
            EmployeeRoleExample roleExample = new EmployeeRoleExample();
            EmployeeRoleExample.Criteria criteria1 = roleExample.createCriteria();
            criteria1.andRidEqualTo(rid);
            employeeRoleMapper.deleteByExample(roleExample);
            //删除角色本身
            roleMapper.deleteByPrimaryKey(rid);
            result.setSuccess(true);
            result.setMessage("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            result.setMessage("操作失败");
            result.setSuccess(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;

        }
        logger.info("删除方法结束，出参："+result);
        return result;
    }

    @Override
    @Transactional
    public BaseResult batchDelete(String rids) {
        BaseResult result = new BaseResult();
        logger.info("批量删除，入参："+rids);
        try {
            String[] ridArray=rids.split(",");
            for (String rid:ridArray){
                BaseResult result1 = deleteRoleByRid(Integer.parseInt(rid));
                //如果其中一条删除失败，抛出异常并回滚事物
                if (!result1.isSuccess()){
                    throw new Exception();
                }
            }
            result.setSuccess(true);
            result.setMessage("删除成功！");
        }catch (Exception e){

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setMessage("批量删除失败");
            result.setSuccess(false);
        }
        logger.info("批量删除结束，出参："+result);

        return result;
    }

    @Override
    public List<Role> getAllRoles() {
        RoleExample roleExample = new RoleExample();
        List<Role> roles = roleMapper.selectByExample(roleExample);
        return roles;
    }



}
