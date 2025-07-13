package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param employeeLoginDTO 登录信息 DTO
     * @return REST 返回结果
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录 (包含管理员)")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("[Controller] 员工登录: {}", employeeLoginDTO);

        // 1. Web 登录
        Employee employee = employeeService.login(employeeLoginDTO);

        // 2. 登录成功后，生成 jwt 令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     * @return REST 返回结果
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出 (包含管理员)")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增
     * @param employeeDTO 新增的员工数据 DTO 对象
     * @return REST 返回结果
     */
    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {

        log.info("[Controller] 新增员工，员工数据: {}", employeeDTO);
        employeeService.save(employeeDTO);

        return Result.success();
    }

    /**
     * 分页查询 员工列表
     * @param employeePageQueryDTO 分页查询参数 DTO 对象
     * @return REST 返回结果
     */
    @GetMapping("/page")
    @ApiOperation("分页查询 员工列表")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){

        log.info("[Controller] 分页查询员工列表，查询参数: {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.page(employeePageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 启用、禁用员工账号
     * @param status 请求路径参数：状态 status
     * @param id 请求 Query 参数：员工 id
     * @return REST 返回结果
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用员工账号")
    public Result<String> startOrStop(@PathVariable Integer status, Long id){

        log.info("[Controller] 启用、禁用员工账号，员工 id: {} 状态: {}", id, status);
        employeeService.startOrStop(status, id);

        return Result.success();
    }

    /**
     * 根据 id 查询员工信息
     * @param id 员工 id
     * @return REST 返回结果
     */
    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询员工信息")
    public Result<Employee> getById(@PathVariable Long id){

        log.info("[Controller] 根据 id 查询员工信息，员工 id: {}", id);
        Employee employee = employeeService.getById(id);

        return Result.success(employee);
    }

    /**
     * 修改员工信息
     * @param employeeDTO 修改员工信息 DTO 对象
     * @return REST 返回结果
     */
    @PutMapping
    @ApiOperation("修改员工信息")
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO){

        log.info("[Controller] 修改员工信息，员工信息: {}", employeeDTO);
        employeeService.update(employeeDTO);

        return Result.success();
    }

    /**
     * 员工修改密码
     * @param passwordEditDTO 修改密码 DTO 对象
     * @return REST 返回结果
     */
    @PutMapping("/editPassword")
    @ApiOperation("员工修改密码")
    public Result<String> editPassword(@RequestBody PasswordEditDTO passwordEditDTO){

        passwordEditDTO.setEmpId(BaseContext.getCurrentId());   // 取出当前员工 id 于 ThreadLocal （前端没写入 EmpId）

        log.info("[Controller] 修改密码，员工 id: {}", passwordEditDTO.getEmpId());
        employeeService.editPassword(passwordEditDTO);

        return Result.success();
    }
}
