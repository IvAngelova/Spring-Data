package com.example.advquerying.repositories;

import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {
    List<Shampoo> findAllBySizeOrderById(Size size);

    List<Shampoo> findAllBySizeOrLabel_IdOrderByPriceAsc(Size size, Long labelId);

    List<Shampoo> findAllByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    int countShampoosByPriceLessThan(BigDecimal price);

    @Query("SELECT sh FROM Shampoo sh JOIN sh.ingredients i WHERE i.name IN :names")
    List<Shampoo> findAllByIngredientsIn(@Param("names") List<String> ingredients);

    @Query("SELECT s FROM Shampoo s WHERE s.ingredients.size < :countIngredients")
    List<Shampoo> findAllShampoosWithIngredientsCountLessThan
            (@Param("countIngredients") int maxCount);
}
