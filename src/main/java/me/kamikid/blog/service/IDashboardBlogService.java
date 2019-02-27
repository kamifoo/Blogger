package me.kamikid.blog.service;

import me.kamikid.blog.entity.Blog;
import me.kamikid.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IDashboardBlogService {
    Blog getBlog(Long id);

    Blog getBlogWithMarkdownToHTML(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Pageable pageable, Long id);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    List<Blog> listRecommendBlogTop(Integer i);

    Page<Blog> searchBlog(String query, Pageable pageable);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();
}
