package me.kamikid.blog.service;

import me.kamikid.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITagService {
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    Tag getTagByName(String name);

    List<Tag> listTagTop(Integer size);
}
