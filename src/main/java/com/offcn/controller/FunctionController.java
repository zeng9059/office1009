package com.offcn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.offcn.bean.Employee;
import com.offcn.bean.Function;
import com.offcn.service.FunctionService;
import com.offcn.util.BaseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("function")
public class FunctionController {
    @Autowired
    private FunctionService functionService;

    @ResponseBody
    @RequestMapping("getCurrentFunctions")
    public JSONArray getCurrentFunctions(HttpSession httpSession){

        Session session = SecurityUtils.getSubject().getSession();
        Employee employee1=(Employee) session.getAttribute("employee");

        Employee employee=(Employee) httpSession.getAttribute("employee");

        List<Function> functions = functionService.getCurrentFunctions(employee1.getEid());
        return functionService.convert2(functions,0);


    }

    /**
     * 根据条件获取功能信息
     * @param function
     * @return
     */

    @ResponseBody
    @RequestMapping("findFuntionByCondition")
    public JSONObject findFuntionByCondition(Function function){

        JSONObject jsonObject = new JSONObject();
        List<Function> functions = functionService.findFuntionByCondition(function);
        int total = functionService.countFunctionsByCondition(function);
        jsonObject.put("rows",functions);
        jsonObject.put("total",total);
        return jsonObject;


    }

    /**
     * 获取系统所有一级功能
     * @return
     */
    @ResponseBody
    @RequestMapping("getFirstFunctions")
    public List<Function> getFirstFunctions(){

        return functionService.getFirstFunctions();
    }

    /**
     * 新增功能
     * @return
     */
    @RequestMapping("addFunction")
    @ResponseBody
    public BaseResult addFunction(Function function){
        BaseResult baseResult = functionService.addFunction(function);
        return baseResult;
    }

    /**
     * 获取要修改的功能信息并存放session
     * @param fid
     * @return
     */
    @ResponseBody
    @RequestMapping("getFunctionDetaile")

    public BaseResult getFunctionDetaile(int fid){

        return functionService.getFunctionDetaile(fid);

    }

    @RequestMapping("findUpdateFunction")
    @ResponseBody
    public Function findUpdateFunction(){
        Session session = SecurityUtils.getSubject().getSession();
        Function function=(Function) session.getAttribute("updateFunction");
        System.out.println("=++++++++++++++++++++++++++++++");
        System.out.println(function.getFid());
        System.out.println(function.getFcode());
        return function;
    }

    /**
     * 修改功能信息
     * @param function
     * @return
     */

    @ResponseBody
    @RequestMapping("updateFunction")

    public BaseResult updateFunction(Function function){
        System.out.println(function.getFid());
        System.out.println(function.getFcode());
        BaseResult baseResult = functionService.updateFunction(function);
        return baseResult;
    }

    @ResponseBody
    @RequestMapping("deleteFunctionByFid")
    public BaseResult deleteFunctionByFid(int fid){
        BaseResult baseResult = functionService.deleteFunctionByFid(fid);
        return baseResult;
    }

    /**
     * 批量删除
     * @param fids
     * @return
     */
    @ResponseBody
    @RequestMapping("batchDelete")
    public BaseResult batchDelete(String fids){
        return functionService.batchDelete(fids);
    }

    /**
     * 根据父级功能id获取信息
     * @param parentId
     * @return
     */
    @RequestMapping("getFunctionsByParentId")
    @ResponseBody
    public List<Function> getFunctionsByParentId(int parentId){
        return  functionService.getFirstFunctionsByParentId(parentId);
    }

    /**
     * 根据角色id获取功能
     */
    @ResponseBody
    @RequestMapping("getFunctionsByRid")
    public List<Function> getFunctionsByEid(Object rid){

        System.out.println(rid.getClass().toString());

        long a=(Long)rid;
        int b=(int)a;
        System.out.println(a);

        return functionService.getFunctionsByEid(b);
    }

}
