package com.denfop.api.recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListRecipes {

   Map<String ,List<BaseMachineRecipe>> map_recipes = new HashMap<>();
   public ListRecipes(String name, List<BaseMachineRecipe> list){
       this.map_recipes.put(name,list);
   }


}
