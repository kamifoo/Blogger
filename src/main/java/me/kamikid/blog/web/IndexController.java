package me.kamikid.blog.web;

import me.kamikid.blog.service.CategoryService;
import me.kamikid.blog.service.DashboardBlogService;
import me.kamikid.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private final DashboardBlogService blogService;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Autowired
    public IndexController(DashboardBlogService blogService, CategoryService categoryService, TagService tagService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String index(@PageableDefault(size=8, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                        Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("categories",categoryService.listCategoryTop(5));
        model.addAttribute("tags",tagService.listTagTop(5));
        model.addAttribute("blogs",blogService.listRecommendBlogTop(5));
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog",blogService.getBlogWithMarkdownToHTML(id));
        return "blog";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size=8, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam String query, Model model){
        model.addAttribute("page",blogService.searchBlog("%"+query+"%", pageable));
        model.addAttribute("query",query);
        return "search";
    }
}
