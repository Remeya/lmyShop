package com.lmy.controller;

import com.lmy.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lmy.pojo.Category;
import com.lmy.service.CategoryService;

import java.util.List;
/*然后返回的是 Page4Navigator 类型，并通过 RestController 转换为 json 对象抛给浏览器。*/
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start",defaultValue = "0")int start,@RequestParam(value = "size",defaultValue = "5")int size)
            throws Exception {
        start = start<0?0:start;
        Page4Navigator<Category> page = categoryService.list(start,size,5);//5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
    }
}
