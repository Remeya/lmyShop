/***整个分类管理都是严格使用 RESTFUL 标准来做的，我们来总结一下。

        1. 资源名称用复数，而非单数。
        即使用 /categories 而不是用 /category

        2. CRUD 分别对应：
        增加： post
        删除： delete
        修改： put
        查询： get

        3. id 参数的传递都用 /id方式。
        如编辑和修改：
        /categories/123

        4. 其他参数采用?name=value的形式
        如分页参数 /categories?start=5

        5. 返回数据
        查询多个返回 json 数组
        增加，查询一个，修改 都返回当前 json 数组
        删除 返回空

        这是分类管理当前用到的 RESTFUL 标准规范。 其他没有用到的，我们暂时就不讲解，后面用到了再总结和整理。*/
package com.lmy.controller;

import com.lmy.util.ImageUtil;
import com.lmy.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lmy.pojo.Category;
import com.lmy.service.CategoryService;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
/***然后返回的是 Page4Navigator 类型，并通过 RestController 转换为 json 对象抛给浏览器。***/
/***这里list和add对应的映射路径都是 categories，但是一个是 GetMapping一个是 PostMapping，REST 规范就是通过method的区别来辨别到底是获取还是增加的。***/
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
    @PostMapping("/categories")
    public Object add(Category bean, MultipartFile image, HttpServletRequest request)throws Exception{
        /*这时候bean的id为空，在进行增加操作以后，因为数据库设置的自增长id，会返回一个id给这个bean的id，所以id不为空，可以研究hinerbrate源码*/
        categoryService.add(bean);
        saveOrUpdateImageFile(bean, image, request);
        return bean;
    }

    private void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request) throws IOException {
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        System.out.println("path:"+request.getServletContext().getRealPath(""));
        File file = new File(imageFolder,bean.getId()+".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img,"jpg",file);
    }

    @DeleteMapping("categories/{id}")
    public String delete(@PathVariable("id") int id,HttpServletRequest request){
        categoryService.delete(id);
        File fileFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(fileFolder,id+".jpg");
        file.delete();
        return null;
    }
    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id){
        Category bean = categoryService.get(id);
        return bean;
    }
    /***增加update函数，它是用 PutMapping 来映射的，因为 REST要求修改用PUT来做。
            1. 获取参数名称
    这里获取参数用的是 request.getParameter("name"). 为什么不用 add里的注入一个 Category对象呢？ 因为。。。PUT 方式注入不了。。。 只能用这种方式取参数了，试了很多次才知道是这么个情况~
            2. 通过 CategoryService 的update方法更新到数据库
            3. 如果上传了图片，调用增加的时候共用的 saveOrUpdateImageFile 方法。
            4. 返回这个分类对象。***/
    @PutMapping("categories/{id}")
    public Object update(Category bean, MultipartFile image, HttpServletRequest request) throws IOException {
        String name = request.getParameter("name");
        bean.setName(name);
        categoryService.update(bean);
        if (image!=null)
            saveOrUpdateImageFile(bean,image,request);
        return bean;
    }
}
