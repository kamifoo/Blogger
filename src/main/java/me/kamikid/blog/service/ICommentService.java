package me.kamikid.blog.service;

import me.kamikid.blog.entity.Comment;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ICommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
