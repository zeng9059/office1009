package com.offcn.service;

import com.offcn.bean.Employee;
import com.offcn.util.BaseResult;
import com.offcn.util.EmployeeResult;

import java.util.List;

public interface EmployeeService {
    EmployeeResult loginCheck(String job_number,String password);


    EmployeeResult shiroLoginCheck(String job_number,String password);

    /**
     * 条件查询员工
     * @param employee
     * @return
     */
    List<Employee> findEmployeeByCondition(Employee employee);

    /**
     * 条件查询员工条数
     * @param employee
     * @return
     */
    int countRowsByCondition(Employee employee);

    /**
     * 新增员工
     * @param employee
     * @return
     */
    BaseResult addEmployee(Employee employee);

    /**
     * 根据id删除员工信息
     * @param eid
     * @return
     */
    BaseResult deleteEmployee(int eid);

    BaseResult getEmployeeRolesByEid(int eid);

    Employee findUpdateEmployee();

    BaseResult updateEmployee(Employee employee);

    BaseResult batchDelete(String rids);

    BaseResult updatePassword(String password1,String password2);

    List<Employee> findAllEmployee();

}
