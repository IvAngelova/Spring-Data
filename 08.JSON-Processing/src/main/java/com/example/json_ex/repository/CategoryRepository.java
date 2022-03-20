package com.example.json_ex.repository;

import com.example.json_ex.model.dto.CategoryStatsDto;
import com.example.json_ex.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT new com.example.json_ex.model.dto.CategoryStatsDto(c.name, count(p), avg(p.price), sum(p.price))" +
            " from Product p" +
            " join p.categories c" +
            " group by c.name" +
            " order by count(p) DESC")
    List<CategoryStatsDto> getCategoryStats();

}
