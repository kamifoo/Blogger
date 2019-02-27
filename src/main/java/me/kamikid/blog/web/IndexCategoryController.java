package me.kamikid.blog.web;

import me.kamikid.blog.entity.Blog;
import me.kamikid.blog.entity.Category;
import me.kamikid.blog.service.ICategoryService;
import me.kamikid.blog.service.IDashboardBlogService;
import me.kamikid.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class IndexCategoryController {

    private final ICategoryService categoryService;

    private final IDashboardBlogService blogService;

    @Autowired
    public IndexCategoryController(ICategoryService categoryService, IDashboardBlogService blogService) {
        this.categoryService = categoryService;
        this.blogService = blogService;
    }

    @GetMapping("/category")
    public String category(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        return category(pageable,-1L,model);
    }

    @GetMapping("/category/{id}")
    public String category(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        List<Category> categories = categoryService.listCategoryTop(1000);
        if (id == -1 && categories.size()!=0) {
            id = categories.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setCategoryId(id);
        blogQuery.setTitle("");
        model.addAttribute("categories", categories);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        return "category";
    }
}
