package com.example.advquerying;

import com.example.advquerying.entities.Size;
import com.example.advquerying.services.IngredientService;
import com.example.advquerying.services.LabelService;
import com.example.advquerying.services.ShampooService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final IngredientService ingredientService;
    private final ShampooService shampooService;
    private final LabelService labelService;
    private final Scanner scanner;

    public ConsoleRunner(IngredientService ingredientService, ShampooService shampooService, LabelService labelService, Scanner scanner) {
        this.ingredientService = ingredientService;
        this.shampooService = shampooService;
        this.labelService = labelService;
        this.scanner = scanner;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Please, enter exercise number: ");
        int exerciseNumber = Integer.parseInt(scanner.nextLine());

        switch (exerciseNumber) {
            case 1 -> selectShampoosBySize();
            case 2 -> selectShampoosBySizeOrLabel();
            case 3 -> selectShampoosByPrice();
            case 4 -> selectIngredientsByName();
            case 5 -> selectIngredientsByNames();
            case 6 -> countShampoosByPrice();
            case 7 -> selectShampoosByIngredients();
            case 8 -> selectShampoosByIngredientsCount();
            case 9 -> deleteIngredientsByName();
            case 10 -> updateIngredientsByPrice();
        }

    }

    private void updateIngredientsByPrice() {
        int affectedRows = ingredientService.updateAllIngredientsByPrice();
        System.out.println(affectedRows);
    }

    private void deleteIngredientsByName() {
        System.out.println("Please, enter ingredient name: ");
        String ingredientName = scanner.nextLine();
        int affectedRows = ingredientService.removeIngredientByGivenName(ingredientName);
        //System.out.println(affectedRows);
    }

    private void selectShampoosByIngredientsCount() {
        System.out.println("Please, enter max ingredients count: ");
        int maxCount = Integer.parseInt(scanner.nextLine());
        shampooService.findShampoosWithIngredientsLessThan(maxCount)
                .forEach(System.out::println);
    }

    private void selectShampoosByIngredients() {
        System.out.println("Please, enter ingredient names: ");
        List<String> input = new ArrayList<>();
        while (true) {
            String line = scanner.nextLine();
            if (line.length() == 0) {
                break;
            }
            input.add(line);
        }

        shampooService.findShampoosWithGivenIngredients(input)
                .forEach(System.out::println);
    }

    private void countShampoosByPrice() {
        System.out.println("Please, enter max price: ");
        BigDecimal maxPrice = new BigDecimal(scanner.nextLine());
        System.out.println(shampooService.findCountOfShampoosWithPriceLowerThan(maxPrice));
    }

    private void selectIngredientsByNames() {
        ingredientService.findIngredientsByGivenNames(List.of("Lavender", "Herbs", "Apple"))
                .forEach(System.out::println);
    }

    private void selectIngredientsByName() {
        System.out.println("Please, enter ingredient name prefix: ");
        String prefix = scanner.nextLine();
        ingredientService.findIngredientsNamesByPrefix(prefix)
                .forEach(System.out::println);
    }

    private void selectShampoosByPrice() {
        System.out.println("Please, enter minimum price: ");
        BigDecimal minPrice = new BigDecimal(scanner.nextLine());
        shampooService.findShampoosWithPriceGreaterThan(minPrice)
                .forEach(System.out::println);
    }

    private void selectShampoosBySizeOrLabel() {
        System.out.println("Please, enter size: ");
        Size size = Size.valueOf(scanner.nextLine().toUpperCase());
        System.out.println("Please, enter label id: ");
        long labelId = Integer.parseInt(scanner.nextLine());
        shampooService.findShampoosBySizeOrLabelId(size, labelId)
                .forEach(System.out::println);

    }

    private void selectShampoosBySize() {
        System.out.println("Please, enter size: ");
        Size size = Size.valueOf(scanner.nextLine().toUpperCase());
        this.shampooService.findShampoosBySize(size)
                .forEach(System.out::println);
    }
}
