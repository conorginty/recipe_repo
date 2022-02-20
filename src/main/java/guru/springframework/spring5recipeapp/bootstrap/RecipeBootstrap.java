package guru.springframework.spring5recipeapp.bootstrap;

import guru.springframework.spring5recipeapp.domain.*;
import guru.springframework.spring5recipeapp.repositories.CategoryRepository;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import guru.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>(2);

        UnitOfMeasure each = findUnitOfMeasure("Each");
        UnitOfMeasure tablespoon = findUnitOfMeasure("Tablespoon");
        UnitOfMeasure teaspoon = findUnitOfMeasure("Teaspoon");
        UnitOfMeasure dash = findUnitOfMeasure("Dash");
        UnitOfMeasure pint = findUnitOfMeasure("Pint");
        UnitOfMeasure cup = findUnitOfMeasure("Cup");

        Category american = findCategory("American");
        Category mexican = findCategory("Mexican");


        Recipe perfectGuacamole = newRecipeWithoutIngredients("Perfect Guacamole", 10, 0, Difficulty.EASY,
                "PERFECT GUAC DIRECTIONS");

        addIngredientToRecipe(perfectGuacamole, new Ingredient("Ripe avocados", new BigDecimal(2), each));
        addIngredientToRecipe(perfectGuacamole, new Ingredient("Kosher salt", new BigDecimal(".5"), teaspoon));
        addIngredientToRecipe(perfectGuacamole, new Ingredient("Fresh lime juice or lemon juice", new BigDecimal(2), tablespoon));
        addIngredientToRecipe(perfectGuacamole, new Ingredient("Minced red onion or thinly sliced green onion", new BigDecimal(2), tablespoon));
        addIngredientToRecipe(perfectGuacamole, new Ingredient("Serrano chilis, stems and seeds removed, minced", new BigDecimal(2), each));
        addIngredientToRecipe(perfectGuacamole, new Ingredient("Cilantro", new BigDecimal(2), tablespoon));
        addIngredientToRecipe(perfectGuacamole, new Ingredient("Freshly grated black pepper", new BigDecimal(2), dash));
        addIngredientToRecipe(perfectGuacamole, new Ingredient("Ripe tomato, seeds and pulp removed, chopped", new BigDecimal(".5"), each));

        perfectGuacamole.getCategories().add(american);
        perfectGuacamole.getCategories().add(mexican);

        addNotesToRecipe(perfectGuacamole, "Perfect Guac Recipe Notes");

        recipes.add(perfectGuacamole);

        return recipes;
    }

    private void addNotesToRecipe(Recipe recipe, String notesContent) {
        Notes notes = new Notes();
        notes.setRecipeNotes(notesContent);
        recipe.setNotes(notes);
        notes.setRecipe(recipe);
    }

    private void addIngredientToRecipe(Recipe recipe, Ingredient ingredient) {
        recipe.getIngredients().add(ingredient);
        ingredient.setRecipe(recipe);
    }

    private Category findCategory(String categoryName) {
        return categoryRepository
                .findByDescription(categoryName)
                .orElseThrow(() -> new RuntimeException("Expected Category not found"));
    }

    private UnitOfMeasure findUnitOfMeasure(String uomName) {
        return unitOfMeasureRepository
                .findByDescription(uomName)
                .orElseThrow(() -> new RuntimeException("Expected Unit of Measure not found"));
    }

    private Recipe newRecipeWithoutIngredients(String description, int prepTime, int cookTime, Difficulty difficulty, String directions) {
        Recipe recipe = new Recipe();
        recipe.setDescription(description);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setDifficulty(difficulty);
        recipe.setDirections(directions);
        return recipe;
    }


}
