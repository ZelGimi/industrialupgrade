package com.denfop.api.inv;


import com.denfop.api.recipe.RecipeOutput;

public interface IInvSlotProcessable {

    RecipeOutput process();

    void consume();

    boolean isEmpty();


}
