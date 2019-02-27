package me.kamikid.blog.service;

import me.kamikid.blog.dao.IDashboardBlogRepository;
import me.kamikid.blog.entity.Blog;
import me.kamikid.blog.entity.Category;
import me.kamikid.blog.exception.BlogNotFoundException;
import me.kamikid.blog.util.MarkdownUtils;
import me.kamikid.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class DashboardBlogService implements IDashboardBlogService {

    private final IDashboardBlogRepository blogRepository;

    @Autowired
    public DashboardBlogService(IDashboardBlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public Blog getBlog(Long id) {
        Blog temp = blogRepository.getOne(id);
        if (temp == null) {
            throw new BlogNotFoundException("Blog Not Found");
        }
        return temp;
    }

    @Override
    public Blog getBlogWithMarkdownToHTML(Long id) {
        blogRepository.updateViews(id);
        Blog temp = blogRepository.getOne(id);
        if (temp == null) {
            throw new BlogNotFoundException("Blog Not Found");
        }
        Blog markdown = new Blog();
        BeanUtils.copyProperties(temp, markdown);
        String content = markdown.getContent();
        content = MarkdownUtils.markdownToHtmlExtensions(content);
        markdown.setContent(content);
        return markdown;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (blog.getTitle() == null) {
                    if(blog.getCategoryId()==null){
                        return null;
                    }
                }
                if (!blog.getTitle().isEmpty() && blog.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getCategoryId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Category>get("category").get("id"), blog.getCategoryId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }



    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long id) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),id);
            }
        },pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setView(0);
        if (blog.getFlag() == null || blog.getFlag().equalsIgnoreCase("")) {
            blog.setFlag("Original");
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog temp = blogRepository.getOne(id);
        if (temp == null) {
            throw new BlogNotFoundException("Blog Not Found");
        }
        blog.setCreateTime(temp.getCreateTime());
        blog.setView(temp.getView());
        BeanUtils.copyProperties(blog, temp);
        temp.setUpdateTime(new Date());
        return blogRepository.save(temp);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Page<Blog> searchBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
