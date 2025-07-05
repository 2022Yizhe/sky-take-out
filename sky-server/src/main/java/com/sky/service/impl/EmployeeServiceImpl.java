package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // md5 加密处理原密码，再进行密码比较
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // original -(parse)-> md5
        if (!password.equals(employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        // 对象属性拷贝（等效于一次性 set 所有共有属性）：属性名一致时，前对象属性值会复制给后对象属性
        BeanUtils.copyProperties(employeeDTO, employee);

        // 密码默认值（把 default-password 用 md5 加密）
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 状态默认值（正常）
        employee.setStatus(StatusConstant.ENABLE);
        // 创建时间、更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        // 创建人、更新人 ID
        employee.setCreateUser(BaseContext.getCurrentId()); // 取出当前员工 id 于 ThreadLocal
        employee.setUpdateUser(BaseContext.getCurrentId()); // 取出当前员工 id 于 ThreadLocal

        employeeMapper.insert(employee);
    }

    /**
     * 分页查询 员工列表
     */
    @Override
    public PageResult page(EmployeePageQueryDTO employeePageQueryDTO) {
        // 使用分页查询插件
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        // 调用分页查询方法，封装为 PageResult 对象
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用、禁用员工账号
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // update employee set status = ? where id = ?

        // 使用 lombok 提供的 builder 方法来创建 Employee 对象
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据 id 查询员工信息
     */
    @Override
    public Employee getById(Long id) {

        // 注意：隐藏密码
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * 修改员工信息
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        // update employee set id = ?, ...  where id = ?

        Employee employee = new Employee();

        // 对象属性拷贝（等效于一次性 set 所有共有属性）：属性名一致时，前对象属性值会复制给后对象属性
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置修改时间、修改人
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId()); // 取出当前员工 id 于 ThreadLocal

        employeeMapper.update(employee);
    }

}
