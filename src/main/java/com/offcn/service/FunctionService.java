package com.offcn.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Function;
import com.offcn.util.BaseResult;

import java.util.List;

public interface FunctionService {

    List<Function> getCurrentFunctions(int eid);

    /**
     * 转换为easyui需要的tree
     * @param functionList
     * @return
     */

    JSONArray convert(List<Function> functionList);

    JSONArray convert2(List<Function> functionList,int parentId);

    /**
     * 根据条件获取功能列表
     * @param function
     * @return
     */

    List<Function> findFuntionByCondition(Function function);
    /**
     * 根据条件获取总的条数
     */
    int countFunctionsByCondition(Function function);

    /**
     * 获取所有一级功能
     * @return
     */
    List<Function> getFirstFunctions();

    /**
     * 新增功能权限
     * @param function
     * @return
     */

    BaseResult addFunction(Function function);

    /**
     * 根据功能id获取功能信息
     * @param fid
     * @return
     */
    BaseResult getFunctionDetaile(int fid);

    /**
     * 修改功能信息
     * @param function
     * @return
     */

    BaseResult updateFunction(Function function);

    /**
     * 根据功能id删除功能
     * @param fid
     * @return
     */

    BaseResult deleteFunctionByFid(int fid);

    /**
     * 批量删除
     * @param fids
     * @return
     */

    BaseResult batchDelete(String fids);

    /**
     * 根据父级功能获取功能信息
     * @param parentId
     * @return
     */

    List<Function> getFirstFunctionsByParentId(int parentId);

    /**
     * 根据角色id获取功能信息
     * @param eid
     * @return
     */
    List<Function> getFunctionsByEid(int eid);

    public List<Function> findAllFunctions();


    JSONArray convert3(List<Function> functionList,List<Function> roleFunctions ,int parentId);



}
