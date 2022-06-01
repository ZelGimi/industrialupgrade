package com.denfop.api.recipe;

public interface IBaseRecipe {

    int getSize();

    String getName();

    boolean consume();

    boolean require();

}
