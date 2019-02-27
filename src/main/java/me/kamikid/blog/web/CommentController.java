package me.kamikid.blog.web;

import me.kamikid.blog.entity.Comment;
import me.kamikid.blog.entity.User;
import me.kamikid.blog.service.CommentService;
import me.kamikid.blog.service.DashboardBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    private final CommentService commentService;

    private final DashboardBlogService blogService;

    @Value("${comment.avatar}")
    private String avatarDefaultUrl;

    @Autowired
    public CommentController(CommentService commentService, DashboardBlogService blogService) {
        this.commentService = commentService;
        this.blogService = blogService;
    }

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String submitComment(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        if(user != null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else{
            comment.setAvatar(avatarDefaultUrl);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+comment.getBlog().getId();
    }

}
