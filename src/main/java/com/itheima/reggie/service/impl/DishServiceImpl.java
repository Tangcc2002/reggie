package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto   包含菜品以及菜品口味的DTO对象
     */
    @Transactional
    public void addWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息
        save(dishDto);
        // 菜品ID
        Long id = dishDto.getId();
        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 设置菜品与口味的对应关系
        flavors = flavors.stream().peek(item -> item.setDishId(id)).collect(Collectors.toList());
        // 保存菜品的口味信息
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据菜品ID查询菜品信息，以及菜品口味
     * @param id    菜品ID信息
     */
    public DishDto queryWithFlavor(Long id) {
        // 查询菜品基本信息
        Dish dish = getById(id);
        // 创建DTO对象
        DishDto dishDto = new DishDto();
        // 将查询到的菜品基本信息拷贝到DTO对象中
        BeanUtils.copyProperties(dish, dishDto);
        // 创建条件构造器
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        // 根据菜品ID查询菜品口味信息
        wrapper.eq(DishFlavor::getDishId, id);
        // 查询菜品口味，并保存到DTO对象当中
        dishDto.setFlavors(dishFlavorService.list(wrapper));
        return dishDto;
    }

    /**
     * 修改菜品信息
     * @param dishDto   包含菜品基本信息以及口味信息的DTO对象
     */
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 修改菜品的基本信息
        updateById(dishDto);
        // 创建条件构造器
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        // 根据菜品ID删除菜品口味信息
        wrapper.eq(DishFlavor::getDishId, dishDto.getId());
        // 删除菜品的口味信息
        dishFlavorService.remove(wrapper);
        // 将菜品口味与菜品管联
        dishDto.setFlavors(dishDto.getFlavors().stream().peek(item -> {
            item.setDishId(dishDto.getId());
        }).collect(Collectors.toList()));
        // 将新的菜品口味信息插入数据库
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }
}
