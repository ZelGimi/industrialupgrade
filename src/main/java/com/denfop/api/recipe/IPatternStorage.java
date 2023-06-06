package com.denfop.api.recipe;

import java.util.List;

public interface IPatternStorage {

    boolean addPattern(RecipeInfo var1);

    List<RecipeInfo> getPatterns();

}
