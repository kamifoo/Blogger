package me.kamikid.blog.service;

import me.kamikid.blog.dao.ITagRepository;
import me.kamikid.blog.entity.Tag;
import me.kamikid.blog.exception.TagNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService implements ITagService {

    public final ITagRepository tagRepository;

    @Autowired
    public TagService(ITagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        Optional<Tag> temp = tagRepository.findById(id);
        return temp.orElse(null);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        if (ids.isEmpty()) {
            return null;
        }
        List<Tag> temp = new ArrayList<>();
        String[] idsString = ids.split(",");
        for (String s : idsString) {
            temp.add(tagRepository.getOne(Long.parseLong(s)));
        }
        return temp;
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException("Unknown Tag");
        }

        Tag temp = tagOptional.get();
        BeanUtils.copyProperties(tag, temp);
        return tagRepository.save(temp);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return tagRepository.findTop(pageable);
    }
}
