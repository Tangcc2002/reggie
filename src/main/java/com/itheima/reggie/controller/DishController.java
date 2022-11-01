package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    @Resource
    private CategoryService categoryService;

    /**
     * 菜品分页查询
     * @param page  查询的页码
     * @param pageSize  查询的数据量
     * @param name  查询菜品的名称
     * @return  返回给前端的分页结果
     */
    @GetMapping("/page")
    public R<Page<DishDto>> getDishList(int page, int pageSize, String name) {
        // 创建分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        // 创建条件构造器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        // 根据条件查询
        dishService.page(pageInfo, wrapper);
        // 拷贝数据
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        dishDtoPage.setRecords(pageInfo.getRecords().stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList()));
        return R.success(dishDtoPage);
    }

    /**
     * @param id    根据菜品ID查询菜品信息
     * @return  菜品信息
     */
    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable Long id) {
        log.info("查询菜品，菜品ID：{}", id);
        return R.success(dishService.queryWithFlavor(id));
    }

    /**
     * 设置菜品是否售卖
     * @param ids    需要修改的菜品的ID
     * @return  修改的结果
     */
    @PostMapping("/status/{status}")
    public R<String> updateDish(Long[] ids, @PathVariable int status) {
        log.info("设置菜品ID:{}状态为{}", ids, status != 0 ? "起售" : "停售");
        for (Long id: ids) {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("修改菜品状态成功");
    }

    /**
     * 删除菜品
     * @param ids   需要删除的菜品的ID
     * @return  删除操作的结果
     */
    @DeleteMapping
    public R<String> deleteDish(Long[] ids) {
        for (Long id: ids) {
            dishService.removeById(id);
        }
        return R.success("菜品删除成功");
    }

    /**
     * 添加菜品
     * @param dishDto 包含菜品以及口味信息的DTO对象
     * @return  添加菜品操作的结果
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.addWithFlavor(dishDto);
        return R.success("添加成功");
    }

    /**
     * 修改菜品
     * @param dishDto   根据前端传递的信息生产的DTO对象
     * @return  修改操作的结果
     */
    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto) {
        log.info("当前正在修改的菜品名：{}", dishDto.getName());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品信息成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish  根据前端传递的信息生成的Dish对象
     * @return  查询的结果
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        log.info("根据条件查询菜品");
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        wrapper.eq(Dish::getStatus, 1);
        return R.success(dishService.list(wrapper));
    }
}
