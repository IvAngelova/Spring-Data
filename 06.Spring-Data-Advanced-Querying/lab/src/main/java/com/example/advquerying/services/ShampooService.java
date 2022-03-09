package com.example.advquerying.services;

import com.example.advquerying.entities.Size;

import java.math.BigDecimal;
import java.util.List;

public interface ShampooService {
    List<String> findShampoosBySize(Size size);

    List<String> findShampoosBySizeOrLabelId(Size size, long labelId);

    List<String> findShampoosWithPriceGreaterThan(BigDecimal minPrice);

    int findCountOfShampoosWithPriceLowerThan(BigDecimal maxPrice);

    List<String> findShampoosWithGivenIngredients(List<String> ingredientNames);

    List<String> findShampoosWithIngredientsLessThan(int maxCount);
}
