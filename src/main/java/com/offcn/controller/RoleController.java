package com.offcn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.offcn.bean.Employee;
import com.offcn.bean.Function;
import com.offcn.bean.Role;
import com.offcn.service.FunctionService;
import com.offcn.service.RoleService;
import com.offcn.util.BaseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("role")
public class RoleController {
    @Autowired
    private FunctionService functionService;

    @Autowired
    private RoleService roleService;


    /**
     * 分页条件获取角色列表
     * @param role
     * @return
     */

    @RequestMapping("findRolesByCondition")
    @ResponseBody
    public JSONObject findRolesByCondition(Role role){
        List<Role> roles = roleService.findRolesByCondition(role);
        int i = roleService.countRowsByCondition(role);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",roles);
        jsonObject.put("total",i);
        return jsonObject;

    }

    /**
     * 添加角色
     * @param role
     * @return
     */

    @ResponseBody
    @RequestMapping("addRole")

    public BaseResult addRole(Role role){
        BaseResult baseResult = roleService.addRole(role);
        return baseResult;
    }

    /**
     * 根据角色id查询具体的角色信息
     * @param rid
     * @return
     */
    @ResponseBody
    @RequestMapping("getDetailRole")
    public BaseResult getDetailRole(int rid){
       return roleService.getDetailRole(rid);
    }

    @RequestMapping("getDetailRoleByRid")
    @ResponseBody
    public Role getDetailRoleByRid(){
        Session session = SecurityUtils.getSubject().getSession();
        Role role=(Role) session.getAttribute("role");
        return role;
    }

    /**
     * 获取角色功能信息存放到session
     * @param rid
     * @return
     */
    @RequestMapping("getRoleFunctionDetaile")
    @ResponseBody

    public BaseResult getRoleFunctionDetaile(int rid){
        Role role = roleService.getRoleFunctionDetaile(rid);
        BaseResult baseResult = new BaseResult();
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("updateRole",role);
        baseResult.setSuccess(true);
        return baseResult;
    }

    @ResponseBody
    @RequestMapping("getRoleFunctions")

    public Role getRoleFunctions(){
        Session session = SecurityUtils.getSubject().getSession();
        Role role=(Role) session.getAttribute("updateRole");
        return role;
    }

    @RequestMapping("roleFunctions")
    @ResponseBody
    public JSONArray roleFunctions(){
        List<Function> functions=functionService.findAllFunctions();
        //获取角色功能信息
        Session session = SecurityUtils.getSubject().getSession();
        Role role=(Role) session.getAttribute("updateRole");
        List<Function> functions1=role.getFunctionList();


        JSONArray jsonArray = functionService.convert3(functions, functions1, 0);
        return jsonArray;
    }

    /**
     * 修改角色功能
     * @param role
     * @return
     */

    @ResponseBody
    @RequestMapping("updateRole")
    public BaseResult updateRole(Role role){
        BaseResult baseResult = roleService.updateRole(role);
        System.out.println(baseResult);
        return baseResult;
    }

    /**
     * 根据角色id删除相关信息
     * @param rid
     * @return
     */

    @RequestMapping("deleteRoleByFid")
    @ResponseBody

    public BaseResult deleteRoleByFid(int rid){
        BaseResult baseResult = roleService.deleteRoleByRid(rid);
        return baseResult;
    }

    /**
     * 批量删除角色
     * @param rids
     * @return
     */

    @ResponseBody
    @RequestMapping("batchDelete")
    public BaseResult batchDelete(String rids){
        BaseResult baseResult = roleService.batchDelete(rids);
        return baseResult;

    }
    @RequestMapping("getAllRoles")
    @ResponseBody
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();

    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
//    @ResponseBody
//    @RequestMapping("addEmployee")
//     public BaseResult addEmployee(Employee employee){
//        roleService.
//     }

}
