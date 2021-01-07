package com.offcn.dao;

import com.offcn.bean.Role;
import com.offcn.bean.RoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    long countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer rid);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer rid);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    /**
     * 根据用户id获取角色信息
     * @param eid
     * @return
     */
    List<Role> findRolesByEid(@Param("eid") int eid);

    /**
     * 分页条件获取角色信息
     * @param role
     * @return
     */

    List<Role> findRolesByCondition(Role role);

    /**
     * 条件统计条数
     * @param role
     * @return
     */

    int countRowsByCondition(Role role);




}