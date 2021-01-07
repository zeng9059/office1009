package com.offcn.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.offcn.bean.Function;
import com.offcn.bean.FunctionExample;
import com.offcn.bean.RoleFunctionExample;
import com.offcn.dao.FunctionMapper;
import com.offcn.dao.RoleFunctionMapper;
import com.offcn.util.BaseResult;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private RoleFunctionMapper roleFunctionMapper;

    private Logger logger = Logger.getLogger(FunctionServiceImpl.class);
    @Autowired
    private FunctionMapper functionMapper;

    @Override
    public List<Function> getCurrentFunctions(int eid) {
        List<Function> functions = functionMapper.getCurrentFunctions(eid);
        return functions;
    }

    public JSONArray convert(List<Function> functionList) {
        JSONArray jsonArray = new JSONArray();
        for (Function function : functionList) {
            if (function.getParentId() == 0) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", function.getFid());
                jsonObject.put("text", function.getFname());

                jsonObject.put("url", function.getFurl());

                JSONArray children = new JSONArray();
                for (Function function1 : functionList) {
                    if (function1.getParentId() == function.getFid()) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("id", function1.getFid());
                        jsonObject1.put("text", function1.getFname());
                        jsonObject1.put("url", function1.getFurl());
                        children.add(jsonObject1);

                    }
                }
                jsonObject.put("children", children);
                jsonArray.add(jsonObject);
            }

        }
        return jsonArray;
    }


    @Override
    public JSONArray convert2(List<Function> functionList,int parentId){
        JSONArray jsonArray = new JSONArray();
        for (Function function:functionList){
            if (function.getParentId()==parentId){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", function.getFid());
                jsonObject.put("text", function.getFname());

                if ("NO".equals(function.getRemark1())){
                    JSONArray children=convert2(functionList,function.getFid());
                    jsonObject.put("children",children);
                    jsonObject.put("state","closed");
                }else {
                    jsonObject.put("state","open");
                    jsonObject.put("url", function.getFurl());
                }
                jsonArray.add(jsonObject);
            }
        }
        return  jsonArray;


    }

    @Override
    public List<Function> findFuntionByCondition(Function function) {
        //根据条件获取功能的条数
        function.setLimitStart((function.getPage() - 1) * function.getRows());
        List<Function> funtionByCondition = functionMapper.findFuntionByCondition(function);
        return funtionByCondition;

    }

    @Override
    public int countFunctionsByCondition(Function function) {
        int i = functionMapper.countFunctionsByCondition(function);
        return i;
    }

    @Override
    public List<Function> getFirstFunctions() {
        FunctionExample example = new FunctionExample();
        FunctionExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(0);
        List<Function> functions = functionMapper.selectByExample(example);
        return functions;
    }

    @Override
    public BaseResult addFunction(Function function) {
        logger.info("进入新增功能权限方法，入参" + function);
        System.out.println(function.getFcode());
        System.out.println(function.getFname());
        System.out.println("父级id" + function.getParentId());
        BaseResult baseResult = new BaseResult();

        try {
            //校验功能编码是否唯一
            FunctionExample functionExample = new FunctionExample();
            FunctionExample.Criteria criteria = functionExample.createCriteria();
            criteria.andFcodeEqualTo(function.getFcode());
            System.out.println(function.getFcode());
            List<Function> functions = functionMapper.selectByExample(functionExample);
            if (functions != null && functions.size() > 0) {
                baseResult.setMessage("功能重复");
                baseResult.setSuccess(false);
                return baseResult;
            }
            //新增功能
            if (function.getFlevel() == 1) {
                //一级功能
                function.setParentId(0);
                function.setRemark2("一级功能");
            } else {
                //二级功能
                //获取二级功能的父级功能名称
                Function parent = functionMapper.selectByPrimaryKey(function.getParentId());
                function.setRemark2(parent.getFname());
            }
            functionMapper.insert(function);
            baseResult.setSuccess(true);
            baseResult.setMessage("新增成功");

        } catch (Exception e) {
            e.printStackTrace();
            baseResult.setSuccess(false);
            baseResult.setMessage("操作失败");
        }
        logger.info("新增方法结束，出参：" + baseResult);

        return baseResult;
    }

    @Override
    public BaseResult getFunctionDetaile(int fid) {
        Function function = functionMapper.selectByPrimaryKey(fid);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("updateFunction", function);
        BaseResult result = new BaseResult();
        if (function != null) {
            result.setSuccess(true);
        }
        return result;
    }

    @Transactional
    @Override
    public BaseResult updateFunction(Function function) {
        logger.info("进入修改权限方法，入参：" + function);
        BaseResult result = new BaseResult();
        try {

            FunctionExample example = new FunctionExample();
            FunctionExample.Criteria criteria = example.createCriteria();
            criteria.andFcodeEqualTo(function.getFcode());
            criteria.andFidNotEqualTo(function.getFid());
            List<Function> functions = functionMapper.selectByExample(example);

            FunctionExample example1 = new FunctionExample();
            FunctionExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andParentIdEqualTo(function.getFid());
            if (functions != null && functions.size() > 0) {
                result.setMessage("功能编码重复！");
                result.setSuccess(false);
                return result;
            }
            //判断修改之后的功能级别
            if (function.getFlevel() == 1) {
                //修改之后为一级功能
                function.setParentId(0);
                function.setRemark2("一级功能");
            } else {
                //修改之后为二级功能
                //删除该功能下的子功能

                functionMapper.deleteByExample(example1);
                //获取二级功能的父级功能名称
                Function function1 = functionMapper.selectByPrimaryKey(function.getParentId());
                function.setRemark2(function1.getRemark2());

            }

            functionMapper.updateByPrimaryKey(function);

            List<Function> functions1 = functionMapper.selectByExample(example1);
            for (Function function1 : functions1) {
                function1.setRemark2(function.getFname());
                functionMapper.updateByPrimaryKey(function1);
            }
            result.setSuccess(true);
            result.setMessage("操作成功");


        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("修改失败");
            result.setSuccess(false);
            //手动回滚事物
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        logger.info("修改方法结束，出参：" + result);
        return result;
    }


    @Transactional
    @Override
    public BaseResult deleteFunctionByFid(int fid) {
        logger.info("进入删除功能权限。入参：" + fid);
        BaseResult result = new BaseResult();

        try {
            //获取该功能的子功能
            FunctionExample example = new FunctionExample();
            FunctionExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(fid);
            List<Function> functions = functionMapper.selectByExample(example);
            //删除子功能的功能角色关系表数据
            if (functions != null && functions.size() > 0) {
                for (Function function : functions) {
                    RoleFunctionExample roleFunctionExample = new RoleFunctionExample();
                    RoleFunctionExample.Criteria criteria1 = roleFunctionExample.createCriteria();
                    criteria1.andFidEqualTo(function.getFid());
                    roleFunctionMapper.deleteByExample(roleFunctionExample);
                    //删除该功能的所有子功能
                    functionMapper.deleteByPrimaryKey(function.getFid());
                }
            }
            //删除该功能本身的功能角色关系表数据
            RoleFunctionExample roleFunctionExample = new RoleFunctionExample();
            RoleFunctionExample.Criteria criteria1 = roleFunctionExample.createCriteria();
            criteria1.andFidEqualTo(fid);
            roleFunctionMapper.deleteByExample(roleFunctionExample);
            //删除该功能本身
            functionMapper.deleteByPrimaryKey(fid);
            result.setSuccess(true);
            result.setMessage("删除成功");

        } catch (Exception e) {

            result.setMessage("操作失败");
            result.setSuccess(false);
            //手动回滚事物
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }
        return result;

    }

    @Override
    @Transactional
    public BaseResult batchDelete(String fids) {
        BaseResult result1 = new BaseResult();
        logger.info("批量删除开始，入参:"+fids);
        try {
            String[] fidArray = fids.split(",");
            for (String fid:fidArray){
                BaseResult result = deleteFunctionByFid(Integer.parseInt(fid));
                if (!result.isSuccess()){
                    throw new Exception();
                }
            }
            result1.setSuccess(true);
            result1.setMessage("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result1.setMessage("操作失败");
            result1.setSuccess(true);
        }
        logger.info("方法结束，出参："+result1);

        return result1;
    }

    @Override
    public List<Function> getFirstFunctionsByParentId(int parentId) {
        FunctionExample functionExample=new FunctionExample();
        FunctionExample.Criteria criteria = functionExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<Function> functions = functionMapper.selectByExample(functionExample);
        return functions;
    }

    @Override
    public List<Function> getFunctionsByEid(int eid) {
        List<Function> functions = functionMapper.getFunctionsByEid(eid);
        return functions;
    }

    @Override
    public List<Function> findAllFunctions() {
        List<Function> functions = functionMapper.selectByExample(new FunctionExample());
        return functions;
    }

    @Override
    public JSONArray convert3(List<Function> functionList,List<Function> roleFunctions ,int parentId) {

        JSONArray jsonArray = new JSONArray();
        for (Function function:functionList){
            //获取指定级功能
            if (function.getParentId()==parentId){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",function.getFid());
                jsonObject.put("text",function.getFname());
                jsonObject.put("state","open");
                if ("NO".equals(function.getRemark1())){
                    JSONArray children=convert3(functionList,roleFunctions,function.getFid());
                    jsonObject.put("children",children);

                }else {
                    jsonObject.put("checked",false);

                    for (Function function1:roleFunctions){
                        if (function.getFid()==function1.getFid()){

                            jsonObject.put("checked",true);
                        }
                    }
                }
                jsonArray.add(jsonObject);


            }
        }
        return jsonArray;
    }

}
