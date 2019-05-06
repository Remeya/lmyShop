package com.lmy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lmy.pojo.Category;
import com.lmy.service.CategoryService;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> list() throws Exception {
        System.out.println("hh");
        return categoryService.list();
    }
}
