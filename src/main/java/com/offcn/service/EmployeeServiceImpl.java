package com.offcn.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.offcn.bean.Employee;
import com.offcn.bean.EmployeeExample;
import com.offcn.bean.EmployeeRole;
import com.offcn.bean.EmployeeRoleExample;
import com.offcn.dao.EmployeeMapper;
import com.offcn.dao.EmployeeRoleMapper;
import com.offcn.util.BaseResult;
import com.offcn.util.EmployeeResult;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRoleMapper employeeRoleMapper;

    Logger logger=Logger.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public EmployeeResult loginCheck(String job_number, String password) {
        EmployeeResult result = new EmployeeResult();
        try {

            EmployeeExample employeeExample = new EmployeeExample();
            EmployeeExample.Criteria criteria = employeeExample.createCriteria();
            criteria.andJobNumberEqualTo(job_number);
            criteria.andPasswordEqualTo(password);
            List<Employee> employees = employeeMapper.selectByExample(employeeExample);
            if(employees!=null&&employees.size()>0){
                result.setLoginSuccess(true);
                result.setMessage("登陆成功");
                result.setEmployee(employees.get(0));
            }else {
                result.setMessage("登陆失败");
                result.setLoginSuccess(false);
            }

            result.setSuccess(true);
        }catch (Exception e){

            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("操作失败");
        }
        return result;

    }

    @Override
    public EmployeeResult shiroLoginCheck(String job_number, String password) {


        EmployeeResult result=new EmployeeResult();
        //创建登录对象
        Subject subject = SecurityUtils.getSubject();

        try{
            subject.login(new UsernamePasswordToken(job_number,password));
            result.setSuccess(true);
            result.setMessage("登陆成功");
            result.setLoginSuccess(true);
            Session session = subject.getSession();
            session.setAttribute("employee",subject.getPrincipal());



        }catch (Exception e){
            result.setLoginSuccess(false);
            result.setMessage("操作失败");
            result.setSuccess(true);


        }
        return result;

    }

    @Override
    public List<Employee> findEmployeeByCondition(Employee employee) {
        employee.setLimitStart((employee.getPage()-1)*employee.getRows());
        List<Employee> employeeByCondition = employeeMapper.findEmployeeByCondition(employee);
        return  employeeByCondition;
    }

    @Override
    public int countRowsByCondition(Employee employee) {
        return employeeMapper.countRowsByCondition(employee);
    }
//
    @Transactional
    @Override
    public BaseResult addEmployee(Employee employee) {
        BaseResult result = new BaseResult();
        logger.info("新增员工：入参："+employee);
        try {
            EmployeeExample example = new EmployeeExample();
            EmployeeExample.Criteria criteria = example.createCriteria();
            criteria.andJobNumberEqualTo(employee.getJobNumber());
            List<Employee> employees = employeeMapper.selectByExample(example);
            if (employees!=null&&employees.size()>0){
                result.setSuccess(false);
                result.setMessage("工号重复");
                return result;
            }

            //新增员工前对密码进行加密处理
            String salt=new SecureRandomNumberGenerator().nextBytes().toHex();
            String newpassword=new Md5Hash(employee.getPassword(),salt,3).toString();
            employee.setPassword(newpassword);
            employee.setRemark1(salt);


            System.out.println(employee.getEid()+"====================================");
            //新增员工
            employeeMapper.insert(employee);

            //获取新增后的员工信息
            EmployeeExample example1 = new EmployeeExample();
            EmployeeExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andJobNumberEqualTo(employee.getJobNumber());
            Employee employee1 = employeeMapper.selectByExample(example1).get(0);

            System.out.println(employee.getEid()+"====================================");
            //新增员工角色表
            String[] rids=employee.getRids().split(",");
            for(String rid:rids){
                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole.setRid(Integer.parseInt(rid));
                employeeRole.setEid(employee1.getEid());
                employeeRoleMapper.insert(employeeRole);

            }

            result.setSuccess(true);
            result.setMessage("新增成功");

        }catch (Exception e){
            e.printStackTrace();
            result.setMessage("新增失败");
            result.setSuccess(false);
            //手动回滚事物
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }

        logger.info("新增方法结束，出参："+result);
        return  result;
    }

    @Transactional
    @Override
    public BaseResult deleteEmployee(int eid) {
        logger.info("删除员工开始，入参："+eid);
        BaseResult baseResult = new BaseResult();

        try {
            //删除员工角色关系表
            EmployeeRoleExample example = new EmployeeRoleExample();
            EmployeeRoleExample.Criteria criteria = example.createCriteria();
            criteria.andEidEqualTo(eid);
            employeeRoleMapper.deleteByExample(example);
            //删除表本身
            employeeMapper.deleteByPrimaryKey(eid);
            baseResult.setMessage("删除成功");
            baseResult.setSuccess(true);

        }
        catch (Exception e){
            e.printStackTrace();
            baseResult.setSuccess(false);
            baseResult.setMessage("删除失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        logger.info("删除员工结束，出参："+baseResult);
        return  baseResult;
    }

    @Override
    public BaseResult getEmployeeRolesByEid(int eid) {

        BaseResult result = new BaseResult();
        Employee employeeRolesByEid = employeeMapper.getEmployeeRolesByEid(eid);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("employee1",employeeRolesByEid);
        result.setSuccess(true);
        return result;
    }

    @Override
    public Employee findUpdateEmployee() {
        Session session = SecurityUtils.getSubject().getSession();
        Employee employee=(Employee) session.getAttribute("employee1");
        return employee;
    }
    @Transactional
    @Override
    public BaseResult updateEmployee(Employee employee) {

        logger.info("修改员工方法，入参："+employee);
        BaseResult result = new BaseResult();

        try {

            //检验工号是否唯一
            EmployeeExample example = new EmployeeExample();
            EmployeeExample.Criteria criteria = example.createCriteria();
            criteria.andJobNumberEqualTo(employee.getJobNumber());
            criteria.andEidNotEqualTo(employee.getEid());
            List<Employee> employees = employeeMapper.selectByExample(example);
            if (employees!=null && employees.size()>0){
                result.setSuccess(false);
                result.setMessage("工号重复");
                return result;
            }

            employeeMapper.updateByPrimaryKeySelective(employee);
            //删除员工以前的角色信息
            EmployeeRoleExample example1 = new EmployeeRoleExample();
            EmployeeRoleExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andEidEqualTo(employee.getEid());
            employeeRoleMapper.deleteByExample(example1);
            //新增新的员工角色表
            String[] rids=employee.getRids().split(",");
            for (String rid:rids){

                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole.setEid(employee.getEid());
                employeeRole.setRid(Integer.parseInt(rid));
                employeeRoleMapper.insert(employeeRole);
            }

            result.setMessage("修改成功");
            result.setSuccess(true);


        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("修改失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }
        return result;
    }

    @Transactional
    @Override
    public BaseResult batchDelete(String eids) {
        BaseResult result = new BaseResult();
        logger.info("批量删除开始，入参："+eids);
        try {
            String[] ridArray=eids.split(",");
            for (String rid:ridArray){
                BaseResult result1 = deleteEmployee(Integer.parseInt(rid));
                if (!result1.isSuccess()){
                    throw new Exception();
                }
            }
            result.setMessage("操作成功");
            result.setSuccess(true);

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setSuccess(false);
            result.setMessage("操作失败");
        }
        logger.info("批量删除结束，出参："+result);
        return result;
    }

    @Override
    public BaseResult updatePassword(String password1, String password2) {
        BaseResult result = new BaseResult();

        try {
            Session session = SecurityUtils.getSubject().getSession();
            Employee employee=(Employee) session.getAttribute("employee");
            System.out.println(employee+"==================");
            EmployeeExample example = new EmployeeExample();
            EmployeeExample.Criteria criteria = example.createCriteria();
            criteria.andEidEqualTo(employee.getEid());

            //修改密码时对原密码进行加密处理
            String salt1=employee.getRemark1();
            String newpassword1=new Md5Hash(password1,salt1,3).toString();


            criteria.andPasswordEqualTo(newpassword1);
            List<Employee> employees = employeeMapper.selectByExample(example);
            if (employees==null || employees.size()==0){
                result.setMessage("原密码输入错误，请重新输入");
                result.setSuccess(false);
                return result;
            }
            //更新密码
            Employee employee1 = new Employee();
            employee1.setEid(employee.getEid());

            //新增员工前对密码进行加密处理
            String salt=new SecureRandomNumberGenerator().nextBytes().toHex();
            String newpassword=new Md5Hash(password2,salt,3).toString();
            employee1.setPassword(newpassword);
            employee1.setRemark1(salt);


            employeeMapper.updateByPrimaryKeySelective(employee1);
            result.setMessage("修改成功,请重新登录");
            result.setSuccess(true);

        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("修改失败");
        }
        return result;

    }

    @Override
    public List<Employee> findAllEmployee() {

        List<Employee> employees = employeeMapper.findAllEmployee();
        return employees;
    }


}
