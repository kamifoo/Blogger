package me.kamikid.blog.web;

import me.kamikid.blog.service.DashboardBlogService;
import me.kamikid.blog.service.IDashboardBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexArchiveController {

    private final IDashboardBlogService blogService;

    @Autowired
    public IndexArchiveController(IDashboardBlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/archive")
    public String archives(Model model) {
        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCount", blogService.countBlog());
        return "archive";
    }
}
