package com.offcn.dao;

import com.offcn.bean.Role;
import com.offcn.bean.RoleFunction;
import com.offcn.bean.RoleFunctionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleFunctionMapper {
    long countByExample(RoleFunctionExample example);

    int deleteByExample(RoleFunctionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoleFunction record);

    int insertSelective(RoleFunction record);

    List<RoleFunction> selectByExample(RoleFunctionExample example);

    RoleFunction selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoleFunction record, @Param("example") RoleFunctionExample example);

    int updateByExample(@Param("record") RoleFunction record, @Param("example") RoleFunctionExample example);

    int updateByPrimaryKeySelective(RoleFunction record);

    int updateByPrimaryKey(RoleFunction record);

    /**
     *根绝角色id获取角色功能信息
     */
    Role getRoleFunctionDetaile(@Param("rid") int rid);
}