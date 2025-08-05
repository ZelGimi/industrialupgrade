package com.denfop.recipe;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseRecipe;
import com.denfop.api.crafting.PartRecipe;
import com.denfop.api.crafting.RecipeGrid;
import com.denfop.network.packet.CustomPacketBuffer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndustrialShapedRecipeSerializer implements RecipeSerializer<BaseRecipe> {

    @Override
    public BaseRecipe fromJson(ResourceLocation id, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        JsonArray patternArray = GsonHelper.getAsJsonArray(json, "pattern");
        List<String> pattern = new ArrayList<>();
        for (JsonElement line : patternArray) {
            pattern.add(line.getAsString());
        }
        RecipeGrid grid = new RecipeGrid(pattern);

        JsonObject keyJson = GsonHelper.getAsJsonObject(json, "key");
        List<PartRecipe> partRecipes = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : keyJson.entrySet()) {
            String symbol = entry.getKey();
            if (symbol.length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + symbol + "' is not a single character.");
            }
            char character = symbol.charAt(0);

            Ingredient ingredient = Ingredient.fromJson(entry.getValue());
            IInputItemStack input = Recipes.inputFactory.getInput(ingredient);

            partRecipes.add(new PartRecipe(String.valueOf(character), input));
        }

        JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
        ItemStack result = ShapedRecipe.itemStackFromJson(resultJson);

        return new BaseRecipe(id,group,result, grid, partRecipes);
    }





    @Override
    public @Nullable BaseRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf pBuffer) {

        return BaseRecipe.create(id,new CustomPacketBuffer(pBuffer));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, BaseRecipe recipe) {
        recipe.toNetwork(buf);
    }
}
