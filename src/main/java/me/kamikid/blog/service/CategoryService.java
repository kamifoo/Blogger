package me.kamikid.blog.service;

import me.kamikid.blog.dao.ICategoryRepository;
import me.kamikid.blog.entity.Category;
import me.kamikid.blog.exception.CategoryNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category getCategory(Long id) {
        Optional<Category> temp = categoryRepository.findById(id);
        return temp.orElse(null);
    }

    @Transactional
    @Override
    public Page<Category> listCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Category updateCategory(Long id, Category category) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if(!categoryOptional.isPresent()){
            throw new CategoryNotFoundException("Unknown Category");
        }

        Category temp = categoryOptional.get();
        BeanUtils.copyProperties(category, temp);
        return categoryRepository.save(temp);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> listCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> listCategoryTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return categoryRepository.findTop(pageable);
    }
}
