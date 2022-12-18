package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.BaseContext;
import com.example.common.R;
import com.example.pojo.Employee;
import com.example.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
@Transactional
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //員工登錄
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        HttpSession session = request.getSession();

        //1.將頁面提交的密碼(password)進行md5加密處理
        String password=employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根據頁面提交的用戶名(username)查詢數據庫
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);

        //3.如果沒有查詢到則返回登錄失敗結果
        if(emp == null){
            return R.error("登錄失敗!");
        }

        //4.比對密碼，如果不一致則返回登錄失敗結果
        if(!emp.getPassword().equals(password)){
            return R.error("登錄失敗!");
        }

        //5.查看員工狀態，如果為已禁用，則返回員工已禁用結果
        if(emp.getStatus() == 0){
            return R.error("帳號已禁用!");
        }

        //6.登錄成功，將員工id存入Session並返回登錄成功結果
        session.setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    //退出登錄
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的員工id
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return R.success("退出成功!");
    }

    //添加(編輯)員工
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增員工，員工信息:{}",employee.toString());

        //設置初始密碼為123456並進行md5加密處理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //設置創建時間與更新時間 (已使用公共字段自動填充優化)
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //獲得當前登錄用戶的id
        //HttpSession session = request.getSession();
        //Long empId=(Long)session.getAttribute("employee");

        //設置創建人與更新人 (已使用公共字段自動填充優化)
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增員工成功!");
    }

    //分頁查詢
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //建立分頁構造器
        Page pageInfo=new Page(page,pageSize);

        //建立條件構造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();

        //添加過濾條件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //添加排序條件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //執行查詢
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    //根據id修改員工信息(啟用/禁用)
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

        //設置更新人與更新時間 (已使用公共字段自動填充優化)
        //HttpSession session = request.getSession();
        //Long empId=(Long)session.getAttribute("employee");
        //employee.setUpdateUser(empId);

        //employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return R.success("員工信息修改成功");
    }

    //根據id查詢員工信息(頁面數據回顯)
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根據id查詢員工信息...");

        Employee employee = employeeService.getById(id);

        if(employee != null){
            return R.success(employee);
        }
        return R.error("沒有查詢到對應員工信息");
    }
}
