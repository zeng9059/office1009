package com.offcn.service;

import com.offcn.bean.Employee;
import com.offcn.bean.Function;
import com.offcn.bean.Role;
import com.offcn.util.BaseResult;

import java.util.List;

public interface RoleService {
    /**
     * 根据条件获取角色信息
     * @param
     * @return
     */

    List<Role> findRolesByCondition(Role role);

    /**
     * 获取条数
     * @param role
     * @return
     */

    int countRowsByCondition(Role role);

    /**
     * 添加角色
     * @param role
     * @return
     */

    BaseResult addRole(Role role);

    /**
     * 根据角色id查询具体的角色信息
     * @param rid
     * @return
     */

    BaseResult getDetailRole(int rid);



    Role getRoleFunctionDetaile(int rid);

    /**
     * 修改角色信息
     * @param role
     * @return
     */
    BaseResult updateRole(Role role);

    /**
     * 根据角色id删除角色信息
     * @param rid
     * @return
     */
    BaseResult deleteRoleByRid(int rid);

    /**
     * 批量删除角色
     * @param rids
     * @return
     */
    BaseResult batchDelete(String rids);

    /**
     * 获取所有角色
     * @return
     */

    List<Role> getAllRoles();



}
