package com.company.product;

import com.company.product.RecipesErrors.NotFoundException;
import com.company.product.RecipesErrors.RecipeNotGoodException;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface RecipeBookService {
    /** @apiNote {@code GET /recipes/{recipeName}}
     * @throws NotFoundException: The requested recipe was not found.
     * @throws RecipeNotGoodException: The recipe is not good.
     **/
    Recipe getRecipe(RecipeName recipeName) throws NotFoundException, RecipeNotGoodException;
}
