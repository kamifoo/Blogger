package me.kamikid.blog.web;

import me.kamikid.blog.entity.Tag;
import me.kamikid.blog.service.IDashboardBlogService;
import me.kamikid.blog.service.ITagService;
import me.kamikid.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class IndexTagController {

    private final ITagService tagService;

    private final IDashboardBlogService blogService;

    @Autowired
    public IndexTagController(ITagService tagService, IDashboardBlogService blogService) {
        this.tagService = tagService;
        this.blogService = blogService;
    }

    @GetMapping("/tag")
    public String tag(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                      Model model) {
        return tag(pageable, -1L, model);
    }

    @GetMapping("/tag/{id}")
    public String tag(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                      @PathVariable Long id, Model model) {
        List<Tag> tags = tagService.listTagTop(1000);
        if (id == -1 && tags.size() != 0) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(pageable, id));
        model.addAttribute("activeTypeId", id);
        return "tag";
    }
}
