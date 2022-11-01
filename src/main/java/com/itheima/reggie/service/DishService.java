package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应的口味数据
     * @param dishDto   包含菜品以及菜品口味的DTO对象
     */
    void addWithFlavor(DishDto dishDto);

    /**
     * 根据ID查询菜品以及菜品口味
     * @param id    菜品ID信息
     */
    DishDto queryWithFlavor(Long id);

    /**
     * 修改菜品信息
     * @param dishDto   包含菜品基本信息以及口味信息的DTO对象
     */
    void updateWithFlavor(DishDto dishDto);
}
