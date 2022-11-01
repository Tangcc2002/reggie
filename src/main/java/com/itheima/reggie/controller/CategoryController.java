package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService service;

    /**
     * 新增菜品分类或套餐分类
     * @param category  根据前端参数生产的分类对象
     * @return  新增结果
     */
    @PostMapping
    public R<String> saveCategory(@RequestBody Category category) {
        log.info("category：{}", category.getName());
        service.save(category);
        return R.success("添加分类成功");
    }

    /**
     * 菜品分类分页查询
     * @param page  查询的页码
     * @param pageSize  插叙的数据量
     * @return  返回至前端的菜品分类数据
     */
    @GetMapping("/page")
    public R<Page<Category>> getCategoryList(int page, int pageSize) {
        // 创建分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        // 创建条件构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        // 进行分页查询
        service.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据ID删除分类
     * @param ids    菜品ID
     * @return  删除结果
     */
    @DeleteMapping
    public R<String> deleteCategory(Long ids) {
        log.info("删除分类，ID：{}", ids);
        // 根据分类ID删除分类
        service.remove(ids);
        return R.success("分类信息删除成功");
    }

    /**
     * 根据ID修改分类
     * @param category  根据前端传递的参数生产的分类对象
     * @return  修改结果
     */
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        service.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 查询菜品分类列表
     * @param type  查询分类的类型
     * @return  菜品分类列表
     */
    @GetMapping("/list")
    public R<List<Category>> categoryList(Integer type) {
        // 创建条件构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        // 根据type字段查询
        wrapper.eq(Category::getType, type);
        // 根据sort进行排序
        wrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        // 开始查询，并返回结果
        return R.success(service.list(wrapper));
    }
}
