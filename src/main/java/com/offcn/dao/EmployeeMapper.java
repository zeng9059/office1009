package com.offcn.dao;

import com.offcn.bean.Employee;
import com.offcn.bean.EmployeeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    long countByExample(EmployeeExample example);

    int deleteByExample(EmployeeExample example);

    int deleteByPrimaryKey(Integer eid);

    int insert(Employee record);

    int insertSelective(Employee record);

    List<Employee> selectByExample(EmployeeExample example);

    Employee selectByPrimaryKey(Integer eid);

    int updateByExampleSelective(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByExample(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    /**
     * 根据条件查询员工信息
     * @param employee
     * @return
     */

    List<Employee> findEmployeeByCondition(Employee employee);

    /**
     * 条件查询员工条数
     */
    int countRowsByCondition(Employee employee);

    Employee getEmployeeRolesByEid(@Param("eid") int eid);

    List<Employee> findAllEmployee();
}