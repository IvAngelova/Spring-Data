package com.example.advquerying.services.impl;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.repositories.IngredientRepository;
import com.example.advquerying.services.IngredientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }


    @Override
    public List<String> findIngredientsNamesByPrefix(String prefix) {
        return ingredientRepository.findAllByNameStartingWith(prefix)
                .stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findIngredientsByGivenNames(List<String> names) {
        return ingredientRepository.findAllByNameInOrderByPrice(names)
                .stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int removeIngredientByGivenName(String ingredientName) {
        return ingredientRepository.removeIngredientByName(ingredientName);
    }

    @Override
    @Transactional
    public int updateAllIngredientsByPrice() {
        return ingredientRepository.updatePriceOfAllIngredients();
    }
}
