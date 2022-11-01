package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     * @param page 查询的页码
     * @param pageSize 查询的数据量
     * @return  套餐信息
     */
    @GetMapping("/page")
    public R<Page<Setmeal>> list(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        wrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        setmealService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * 添加套餐
     * @param setmealDto    包含套餐基本信息以及套餐包含的菜品
     * @return  添加操作的结果
     */
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("添加套餐成功");
    }

    /**
     * 删除套餐
     * @param ids   包含需要删除套餐ID的数组
     * @return  删除操作的结果
     */
    @DeleteMapping
    public R<String> deleteSetmeal(Long[] ids) {
        setmealService.getBaseMapper().deleteBatchIds(Arrays.asList(ids));
        return R.success("删除套餐成功");
    }

    /**
     * 修改套餐的状态
     * @param state 修改后的状态
     * @param ids   包含需要修改的套餐ID的数组
     * @return  修改套餐操作的结果
     */
    @PostMapping("/status/{state}")
    public R<String> updateStatus(@PathVariable int state, Long[] ids) {
        List<Setmeal> list = Arrays.stream(ids).map(item -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(item);
            setmeal.setStatus(state);
            return setmeal;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(list);
        return R.success("修改套餐状态成功");
    }

    /**
     * 根据ID条件查询套餐
     * @return  套餐信息
     */
    @GetMapping("/{id}")
    public R<SetmealDto> querySetmealById(@PathVariable long id) {
        log.info("test");
        return R.success(setmealService.querySetmeal(id));
    }

    /**
     * 修改套餐
     * @param setmealDto    包含套餐信息以及套餐包含的菜品的DTO对象
     * @return  修改套餐操作的结果
     */
    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.updateSetmeal(setmealDto);
        return R.success("修改套餐成功");
    }
}
