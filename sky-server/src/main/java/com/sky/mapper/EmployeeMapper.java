package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据员工的用户名查询员工
     * @param username 员工的用户名
     * @return 员工
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee 新员工信息
     */
    @Insert("insert into employee " +
            "(username, name, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values " +
            "(#{username}, #{name}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    /**
     * & 动态 SQL
     * 分页查询 员工列表(基于 PageHelper 依赖补充动态 SQL 的 limit 参数)
     * @param employeePageQueryDTO 分页查询参数 DTO
     * @return 页面结果
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * & 动态 SQL
     * 更新员工账号信息
     * @param employee 新的员工信息
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据 id 查询员工信息
     * @param id 员工 id
     * @return 员工信息
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
