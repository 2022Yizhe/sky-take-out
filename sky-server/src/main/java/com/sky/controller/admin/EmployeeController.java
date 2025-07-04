package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成功后，生成 jwt 令牌
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

        log.info("[Controller] 新增员工，员工数据：{}", employeeDTO);
        employeeService.save(employeeDTO);

        return Result.success();
    }
}
