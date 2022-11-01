package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto    包含了套餐基本信息以及套餐菜品信息的DTO对象
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        save(setmealDto);
        setmealDto.setSetmealDishes(setmealDto.getSetmealDishes().stream().peek(item -> {
            item.setSetmealId(setmealDto.getId());
        }).collect(Collectors.toList()));
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    /**
     * 根据套餐ID查询套餐信息
     * @param id    需要查询的套餐ID
     * @return  包含套餐基本信息以及套餐包含的菜品信息
     */
    @Override
    public SetmealDto querySetmeal(long id) {
        Setmeal setmeal = getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        setmealDto.setSetmealDishes(setmealDishService.list(wrapper));

        return setmealDto;
    }

    /**
     * 修改套餐
     * @param setmealDto    包含套餐以及套餐信息的DTO
     */
    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(wrapper);

        setmealDishService.saveBatch(setmealDto.getSetmealDishes().stream().peek(item -> {
            item.setSetmealId(setmealDto.getId());
        }).collect(Collectors.toList()));
    }
}
