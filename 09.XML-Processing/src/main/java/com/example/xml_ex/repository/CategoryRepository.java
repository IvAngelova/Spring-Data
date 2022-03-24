package com.example.xml_ex.repository;

import com.example.xml_ex.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c " +
            "order by c.products.size DESC")
    List<Category> findAllCategoriesByCountOfProducts();
}
