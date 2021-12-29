package com.denfop.api;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

import java.util.Map;

public interface IWitherMaker {

    void addRecipe(
            IRecipeInput container,
            IRecipeInput fill,
            IRecipeInput container1,
            IRecipeInput fill1,
            IRecipeInput fill2,
            IRecipeInput fill3,
            IRecipeInput fill4,
            ItemStack output
    );

    RecipeOutput getOutputFor(
            ItemStack container,
            ItemStack fill,
            ItemStack fill1,
            ItemStack fill2,
            ItemStack container1,
            ItemStack fill3,
            ItemStack fill4,
            boolean adjustInput,
            boolean acceptTest
    );

    Map<IWitherMaker.Input, RecipeOutput> getRecipes();

    class Input {

        public final IRecipeInput container;
        public final IRecipeInput fill;
        public final IRecipeInput container1;
        public final IRecipeInput fill1;
        public final IRecipeInput fill2;
        public final IRecipeInput fill3;
        public final IRecipeInput fill4;

        public Input(
                IRecipeInput container,
                IRecipeInput fill,
                IRecipeInput fill1,
                IRecipeInput fill2,
                IRecipeInput container1,
                IRecipeInput fill3,
                IRecipeInput fill4
        ) {
            this.container = container;
            this.fill = fill;
            this.container1 = container1;
            this.fill1 = fill1;
            this.fill2 = fill2;
            this.fill3 = fill3;
            this.fill4 = fill4;
        }

        public boolean matches(
                ItemStack container,
                ItemStack fill,
                ItemStack fill1,
                ItemStack fill2,
                ItemStack fill3,
                ItemStack fill4,
                ItemStack container1
        ) {
            return this.container.matches(container) && this.fill.matches(fill) && this.fill1.matches(fill1) && this.fill2.matches(
                    fill2) && this.container1.matches(container1) && this.fill3.matches(fill3) && this.fill4.matches(fill4);
        }

    }

}
