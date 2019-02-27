package me.kamikid.blog.web.admin;

import me.kamikid.blog.entity.Blog;
import me.kamikid.blog.entity.Category;
import me.kamikid.blog.entity.User;
import me.kamikid.blog.service.DashboardBlogService;
import me.kamikid.blog.service.CategoryService;
import me.kamikid.blog.service.TagService;
import me.kamikid.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class DashboardBlogController {

    private static final String PUBLISH = "admin/edit-blog";
    private static final String DASHBOARD = "admin/dashboard";
    private static final String REDIRECT_DASHBOARD = "redirect:/admin/dashboard";


    private final DashboardBlogService blogService;

    private final CategoryService categoryService;

    private final TagService tagService;

    @Autowired
    public DashboardBlogController(DashboardBlogService blogService, CategoryService categoryService, TagService tagService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("categories", categoryService.listCategory());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return DASHBOARD;
    }

    @PostMapping("/dashboard/search")
    public String search(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/dashboard :: blogList";
    }

    @GetMapping("/dashboard/publish")
    public String publishBlog(Model model) {
        model.addAttribute("categories", categoryService.listCategory());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        return PUBLISH;
    }

    @PostMapping("/dashboard")
    public String postPublishBlog(Blog blog, HttpSession session, RedirectAttributes attributes) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setCategory(categoryService.getCategory(blog.getCategory().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog temp;
        if (blog.getId() == null) {
            temp = blogService.saveBlog(blog);
        }else {
            temp = blogService.updateBlog(blog.getId(),blog);
        }
        if (temp == null) {
            attributes.addFlashAttribute("message", "Failed to publish blog");
        } else {
            attributes.addFlashAttribute("message", blog.getTitle() + " - Successfully Modified!");
        }
        return REDIRECT_DASHBOARD;
    }

    @GetMapping("/dashboard/{id}/edit")
    public String editBlog(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlog(id);
        blog.initialTags();
        model.addAttribute("blog", blog);
        model.addAttribute("categories", categoryService.listCategory());
        model.addAttribute("tags", tagService.listTag());
        return PUBLISH;
    }

    @GetMapping("/dashboard/{id}/delete")
    public String deleteBlog(@PathVariable Long id, RedirectAttributes attributes) {
        Blog temp = blogService.getBlog(id);
        String title = temp.getTitle();
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", title + " - Deleted...");
        return REDIRECT_DASHBOARD;
    }
}
