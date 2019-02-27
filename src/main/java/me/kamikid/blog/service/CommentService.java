package me.kamikid.blog.service;

import me.kamikid.blog.dao.ICommentRepository;
import me.kamikid.blog.entity.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService implements ICommentService {

    private final ICommentRepository commentRepository;

    @Autowired
    public CommentService(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        List<Comment> comments = commentRepository.findByBlogId(blogId,sort);
        return correctCommentsStructure(comments);
    }

    @Override
    public Comment saveComment(Comment comment) {
        Long parentId = comment.getParentComment().getId();
        if(parentId != -1){
            comment.setParentComment(commentRepository.getOne(parentId));
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }


    private List<Comment> correctCommentsStructure(List<Comment> comments) {
        List<Comment> heads = new ArrayList<>();
        for(Comment c: comments){
            if(c.getParentComment()==null){
                heads.add(c);
            }
        }
        return heads;
    }

}
