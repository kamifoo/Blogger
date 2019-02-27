package me.kamikid.blog.dao;

import me.kamikid.blog.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);


    @Query("SELECT t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
