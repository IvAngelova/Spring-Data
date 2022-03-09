package com.example.advquerying.services.impl;

import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;
import com.example.advquerying.repositories.ShampooRepository;
import com.example.advquerying.services.ShampooService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShampooServiceImpl implements ShampooService {
    private final ShampooRepository shampooRepository;

    public ShampooServiceImpl(ShampooRepository shampooRepository) {
        this.shampooRepository = shampooRepository;
    }

    @Override
    public List<String> findShampoosBySize(Size size) {
        return shampooRepository.findAllBySizeOrderById(size)
                .stream()
                .map(shampoo ->
                        String.format("%s %s %.2flv.",
                                shampoo.getBrand(),
                                shampoo.getSize().name(),
                                shampoo.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findShampoosBySizeOrLabelId(Size size, long labelId) {
        return shampooRepository.findAllBySizeOrLabel_IdOrderByPriceAsc(size, labelId)
                .stream()
                .map(shampoo ->
                        String.format("%s %s %.2flv.",
                                shampoo.getBrand(),
                                shampoo.getSize().name(),
                                shampoo.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findShampoosWithPriceGreaterThan(BigDecimal minPrice) {
        return shampooRepository.findAllByPriceGreaterThanOrderByPriceDesc(minPrice)
                .stream()
                .map(shampoo ->
                        String.format("%s %s %.2flv.",
                                shampoo.getBrand(),
                                shampoo.getSize().name(),
                                shampoo.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public int findCountOfShampoosWithPriceLowerThan(BigDecimal maxPrice) {
        return shampooRepository.countShampoosByPriceLessThan(maxPrice);
    }

    @Override
    public List<String> findShampoosWithGivenIngredients(List<String> ingredientNames) {
        return shampooRepository.findAllByIngredientsIn(ingredientNames)
                .stream()
                .map(Shampoo::getBrand)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findShampoosWithIngredientsLessThan(int maxCount) {
        return shampooRepository.findAllShampoosWithIngredientsCountLessThan(maxCount)
                .stream()
                .map(Shampoo::getBrand)
                .collect(Collectors.toList());
    }
}
