package me.kamikid.blog.service;

import me.kamikid.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Category saveCategory(Category category);

    Category getCategory(Long id);

    Page<Category> listCategory(Pageable pageable);

    List<Category>  listCategory();

    List<Category> listCategoryTop(Integer size);

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);

    Category getCategoryByName(String name);

}
