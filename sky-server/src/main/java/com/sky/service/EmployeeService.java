package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO 登录信息 DTO
     * @return 员工信息
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

    /**
     * 启用、禁用员工账号
     * @param status 员工状态
     * @param id 员工 id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据 id 查询员工信息
     * @param id 员工 id
     * @return 员工信息
     */
    Employee getById(Long id);

    /**
     * 修改员工信息
     * @param employeeDTO 修改员工信息 DTO 对象
     */
    void update(EmployeeDTO employeeDTO);

    /**
     * 员工修改密码
     * @param passwordEditDTO 修改密码 DTO 对象
     */
    void editPassword(PasswordEditDTO passwordEditDTO);
}
