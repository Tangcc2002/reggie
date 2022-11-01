package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Resource
    private OrdersService service;

    /**
     * 订单分压查询
     * @param page  需要查询的页码
     * @param pageSize  插叙的数据量
     * @param name  需要查询的订单号
     * @return  查询页码的订单记录
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(int page, int pageSize, String name) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Orders::getOrderTime);
        return R.success(service.page(pageInfo, wrapper));
    }
}
