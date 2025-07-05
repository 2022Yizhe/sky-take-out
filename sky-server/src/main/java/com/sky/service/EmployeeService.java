package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO 登录信息 DTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO 新员工信息 DTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询 员工列表
     * @param employeePageQueryDTO 分页查询参数 DTO
     * @return 分页结果
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);
}
