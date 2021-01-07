package com.offcn.dao;

import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Function;
import com.offcn.bean.FunctionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FunctionMapper {
    long countByExample(FunctionExample example);

    int deleteByExample(FunctionExample example);

    int deleteByPrimaryKey(Integer fid);

    int insert(Function record);

    int insertSelective(Function record);

    List<Function> selectByExample(FunctionExample example);

    Function selectByPrimaryKey(Integer fid);

    int updateByExampleSelective(@Param("record") Function record, @Param("example") FunctionExample example);

    int updateByExample(@Param("record") Function record, @Param("example") FunctionExample example);

    int updateByPrimaryKeySelective(Function record);

    int updateByPrimaryKey(Function record);

    /**
     * 根据eid'获取当前用户的功能信息
     * @param eid
     * @return
     */

    List<Function> getCurrentFunctions(@Param("eid") int eid);

    /**
     * 根据条件查询功能列表
     */
    List<Function> findFuntionByCondition(Function function);
    /**
     * 根据条件获取功能条数
     */
    int countFunctionsByCondition(Function function);

    /**
     * 根据角色id查询功能信息
     * @param eid
     * @return
     */

    List<Function> getFunctionsByEid(@Param("eid")int eid);


}