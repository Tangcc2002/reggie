package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto    包含了套餐基本信息以及套餐菜品信息的DTO对象
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据ID查询套餐信息
     * @param id    需要查询的套餐ID
     * @return  包含套餐基本信息以及套餐包含的菜品信息
     */
    SetmealDto querySetmeal(long id);

    /**
     * 修改套餐信息
     * @param setmealDto    包含套餐以及套餐信息的DTO
     */
    void updateSetmeal(SetmealDto setmealDto);
}
