package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService service;

    /**
     * 员工登录
     * @param request   用于设置session的Request对象
     * @param employee  根据前端传递的信息封装成的员工对象
     * @return  登录操作的结果
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 根据页面提交的username查询数据库
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = service.getOne(wrapper);

        // 查询结果为空则表示账号不存在
        if (one == null) {
            return R.error("账号或密码错误");
        }

        // 将页面提交的密码进行MD5加密处理
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        // 密码比对不一致，返回登录失败结果
        if (!password.equals(one.getPassword())) {
            return R.error("账号或密码错误");
        }

        // 查看员工账号是否禁用
        if (one.getStatus() == 0)
            return R.error("账号已禁用");

        // 登录成功，将员工ID存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", one.getId());
        return R.success(one);
    }

    /**
     * 员工退出
     * @param request   用于清楚session中存储的ID
     * @return  退出操作的结果
     */
    @PostMapping("/logout")
    public R<String> logOut(HttpServletRequest request) {
        // 清楚当前session中存储的ID
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 员工信息分页查询
     * @param page 查询的页码
     * @param pageSize 查询的数据量
     * @param name  查询的员工姓名
     * @return  员工列表
     */
    @GetMapping("/page")
    public R<Page<Employee>> employeeList(Long page, int pageSize, String name) {
        log.info("page:{}, pageSize:{}, name:{}", page, pageSize, name);
        // 创建分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        // 创建条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        service.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * 新增员工
     * @param employee  根据前端传递的信息，封装成一个员工对象
     * @return  操作的结果
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee) {
        log.info("新增员工，员工信息{}", employee.toString());
        // 设置员工账号的默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 将员工信息插入数据库
        service.save(employee);
        return R.success("员工添加成功");
    }

    /**
     * 修改员工账号
     * @param employee  根据前端传递的信息构造的员工对象
     * @return  修改结果
     */
    @PutMapping
    public R<String> updateStatusEmployee(@RequestBody Employee employee) {
        log.info(employee.toString());
        // 修改员工数据
        service.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据员工ID查询员工的信息
     * @param id    员工ID
     * @return  返回给前端的员工对象
     */
    @GetMapping("/{id}")
    public R<Employee> getEmployee(@PathVariable long id) {
        log.info("根据员工ID查询员工信息...");
        Employee employee = service.getById(id);
        if (employee != null)
            R.success(service.getById(id));
        return R.error("没有查询到对应员工信息");
    }
}
