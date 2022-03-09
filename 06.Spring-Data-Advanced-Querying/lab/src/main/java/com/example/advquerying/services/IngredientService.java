package com.example.advquerying.services;

import java.util.List;

public interface IngredientService {
    List<String> findIngredientsNamesByPrefix(String prefix);

    List<String> findIngredientsByGivenNames(List<String> names);

    int removeIngredientByGivenName(String ingredientName);

    int updateAllIngredientsByPrice();
}
