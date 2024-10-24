package com.company.product;

import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.Contract;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class RecipesErrors {
    /** The requested resource was not found. */
    public static final ErrorType NOT_FOUND = ErrorType.create(ErrorType.Code.NOT_FOUND, "Recipes:NotFound");

    /** The recipe is not good. */
    public static final ErrorType RECIPE_NOT_GOOD = ErrorType.create(ErrorType.Code.INTERNAL, "Recipes:RecipeNotGood");

    private RecipesErrors() {}

    public static NotFoundException notFound(@Safe String resourceName) {
        return new NotFoundException(NOT_FOUND, SafeArg.of("resourceName", resourceName));
    }

    public static NotFoundException notFound(@Nullable Throwable cause, @Safe String resourceName) {
        return new NotFoundException(NOT_FOUND, cause, SafeArg.of("resourceName", resourceName));
    }

    public static RecipeNotGoodException recipeNotGood(@Safe Recipe recipe) {
        return new RecipeNotGoodException(RECIPE_NOT_GOOD, SafeArg.of("recipe", recipe));
    }

    public static RecipeNotGoodException recipeNotGood(@Nullable Throwable cause, @Safe Recipe recipe) {
        return new RecipeNotGoodException(RECIPE_NOT_GOOD, cause, SafeArg.of("recipe", recipe));
    }

    /**
     * Throws a {@link ServiceException} of type NotFound when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param resourceName
     */
    @Contract("true, _ -> fail")
    public static void throwIfNotFound(boolean shouldThrow, @Safe String resourceName) throws NotFoundException {
        if (shouldThrow) {
            throw notFound(resourceName);
        }
    }

    /**
     * Throws a {@link ServiceException} of type RecipeNotGood when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param recipe
     */
    @Contract("true, _ -> fail")
    public static void throwIfRecipeNotGood(boolean shouldThrow, @Safe Recipe recipe) throws RecipeNotGoodException {
        if (shouldThrow) {
            throw recipeNotGood(recipe);
        }
    }

    /** Returns true if the {@link RemoteException} is named Recipes:NotFound */
    public static boolean isNotFound(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return NOT_FOUND.name().equals(remoteException.getError().errorName());
    }

    /** Returns true if the {@link RemoteException} is named Recipes:RecipeNotGood */
    public static boolean isRecipeNotGood(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return RECIPE_NOT_GOOD.name().equals(remoteException.getError().errorName());
    }

    static class NotFoundException extends CheckedServiceException {
        public NotFoundException(ErrorType errorType, Arg<String> resourceName) {
            super(errorType, resourceName);
        }

        public NotFoundException(ErrorType errorType, Throwable cause, Arg<String> resourceName) {
            super(errorType, cause, resourceName);
        }
    }

    static class RecipeNotGoodException extends CheckedServiceException {
        public RecipeNotGoodException(ErrorType errorType, Arg<Recipe> recipeArg) {
            super(errorType, recipeArg);
        }

        public RecipeNotGoodException(ErrorType errorType, Throwable cause, Arg<Recipe> recipeArg) {
            super(errorType, cause, recipeArg);
        }
    }
}
