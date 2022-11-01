package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    /**
     * 根据ID删除菜品分类
     * @param id    菜品分了ID
     */
    public void remove(Long id) {
        // 创建条件构造器
        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        // 将分类ID设置为查询条件
        dishWrapper.eq(Dish::getCategoryId, id);
        // 根据分类ID查询出菜品不为空，则不能删除分类，且抛出异常
        if (dishService.count(dishWrapper) > 0)
            throw new CustomException("当前分类已关联菜品");
        // 创建条件构造器
        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        // 将分类ID设置为查询条件
        setmealWrapper.eq(Setmeal::getCategoryId, id);
        // 根据分类ID查询出套餐不为空，则不能删除分类，且抛出异常
        if (setmealService.count(setmealWrapper) > 0)
            throw new CustomException("当前分类已关联套餐");
        // 分类未关联菜品、套餐，根据分类ID正常删除分类
        removeById(id);
    }
}
