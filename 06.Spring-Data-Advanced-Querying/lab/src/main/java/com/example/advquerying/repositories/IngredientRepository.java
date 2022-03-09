package com.example.advquerying.repositories;

import com.example.advquerying.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByNameStartingWith(String prefix);

    List<Ingredient> findAllByNameInOrderByPrice(Collection<String> names);

    @Modifying
    int removeIngredientByName(String name);

    @Modifying
    @Query("UPDATE Ingredient i SET i.price = i.price * 1.10")
    int updatePriceOfAllIngredients();
}
