package com.offcn.controller;


import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Employee;
import com.offcn.service.EmployeeService;
import com.offcn.util.BaseResult;
import com.offcn.util.EmployeeResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("employee")

public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @ResponseBody
    @RequestMapping("login")
    public EmployeeResult loginCheck(String job_number, String password, HttpSession httpSession){
        
        EmployeeResult employeeResult = employeeService.loginCheck(job_number, password);
        httpSession.setAttribute("employee",employeeResult.getEmployee());
        return employeeResult;

    }

    @ResponseBody
    @RequestMapping("shiroLogin")
    public EmployeeResult shiroLoginCheck(String job_number,String password){
        EmployeeResult employeeResult = employeeService.shiroLoginCheck(job_number, password);

        System.out.println(employeeResult.toString());

        return employeeResult;

    }


    @ResponseBody
    @RequestMapping("getCurentEmployee")
    public Employee getCurentEmployee(){
        Session session = SecurityUtils.getSubject().getSession();
        Employee employee=(Employee) session.getAttribute("employee");
        return employee;
    }

    @ResponseBody
    @RequestMapping("quite")
    public boolean quite(){
        Session session = SecurityUtils.getSubject().getSession();
        session.stop();
        return  true;

    }

    @ResponseBody
    @RequestMapping("findEmployeeByCondition")
    public JSONObject findEmployeeByCondition(Employee employee){
        JSONObject jsonObject = new JSONObject();
        int total=employeeService.countRowsByCondition(employee);
        List<Employee> employees = employeeService.findEmployeeByCondition(employee);
        jsonObject.put("rows",employees);
        jsonObject.put("total",total);
        return jsonObject;

    }

    @ResponseBody
    @RequestMapping("addEmployee")
    public BaseResult  addEmployee(Employee employee){
       return employeeService.addEmployee(employee);
    }

    @ResponseBody
    @RequestMapping("deleteEmployee")
    public BaseResult deleteEmployee(int eid){
        return employeeService.deleteEmployee(eid);
    }

    @ResponseBody
    @RequestMapping("getEmployeeDetaile")
    public BaseResult getEmployeeDetaile(int eid){
        return employeeService.getEmployeeRolesByEid(eid);
    }

    @ResponseBody
    @RequestMapping("findUpdateEmployee")
    public Employee findUpdateEmployee(){
        return  employeeService.findUpdateEmployee();
    }




    @ResponseBody
    @RequestMapping("updateEmployee")
    public BaseResult updateEmployee(Employee employee){
        return employeeService.updateEmployee(employee);
    }

    @RequestMapping("batchDelete")
    @ResponseBody
    public BaseResult batchDelete(String eids){
        System.out.println(eids+"=======================");
        BaseResult result = employeeService.batchDelete(eids);
        return  result;

    }

    @ResponseBody
    @RequestMapping("updatePassword")
    public BaseResult updatePassword(String password1,String password2){

        return employeeService.updatePassword(password1, password2);

    }

    /**
     * 导出
     * @return
     */
    @ResponseBody
    @RequestMapping("export")
    public ResponseEntity<byte[]> export() throws IOException {
        String fileName="employee.xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet1=workbook.createSheet();
        Row title=sheet1.createRow(0);
        title.createCell(0).setCellValue("员工id");
        title.createCell(1).setCellValue("员工姓名");
        title.createCell(2).setCellValue("员工性别");
        title.createCell(3).setCellValue("员工年龄");
        title.createCell(4).setCellValue("联系电话");
        title.createCell(5).setCellValue("入职日期");
        title.createCell(6).setCellValue("工号");
        Employee employee = new Employee();
        employee.setPage(1);
        employee.setRows(9999);
        //获取数据库员工信息
        List<Employee> employees = employeeService.findEmployeeByCondition(employee);
        for (Employee employee1:employees){
            String sex="";
            Row nextRow=sheet1.createRow(sheet1.getLastRowNum()+1);
            nextRow.createCell(0).setCellValue(employee1.getEid());
            nextRow.createCell(1).setCellValue(employee1.getEname());
            if (employee1.getEsex()==0){
                sex="男";
            }else {
                sex="女";
            }
            nextRow.createCell(2).setCellValue(sex);
            nextRow.createCell(3).setCellValue(employee1.getEage());
            nextRow.createCell(4).setCellValue(employee1.getPhone());
            nextRow.createCell(5).setCellValue(employee1.getHireDate().toString());
            nextRow.createCell(6).setCellValue(employee1.getJobNumber());
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attachment",fileName);


        return new ResponseEntity(bytes,httpHeaders, HttpStatus.OK);



    }


    @ResponseBody
    @RequestMapping("findAllEmployee")
    public List<Employee> findAllEmployee(){
        return employeeService.findAllEmployee();
    }





}
