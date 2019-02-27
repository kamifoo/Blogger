package me.kamikid.blog.dao;

import me.kamikid.blog.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    @Query("SELECT c from Category c")
    List<Category> findTop(Pageable pageable);
}
