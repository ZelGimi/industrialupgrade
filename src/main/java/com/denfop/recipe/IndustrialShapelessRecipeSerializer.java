package com.denfop.recipe;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseShapelessRecipe;
import com.denfop.network.packet.CustomPacketBuffer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IndustrialShapelessRecipeSerializer implements RecipeSerializer<BaseShapelessRecipe> {

    @Override
    public BaseShapelessRecipe fromJson(ResourceLocation id, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        CraftingBookCategory category = CraftingBookCategory.CODEC.byName(
                GsonHelper.getAsString(json, "category", null),
                CraftingBookCategory.MISC
        );


        JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");
        if (ingredientsJson.size() == 0) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        }
        if (ingredientsJson.size() > 3 * 3) {
            throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is " + (9));
        }

        List<IInputItemStack> inputList = new ArrayList<>();
        for (JsonElement element : ingredientsJson) {
            Ingredient ingredient = Ingredient.fromJson(element, false);
            IInputItemStack input = Recipes.inputFactory.getInput(ingredient);
            inputList.add(input);
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));


        return new BaseShapelessRecipe(id, group, category, result, inputList);
    }


    @Override
    public @Nullable BaseShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf pBuffer) {

        return BaseShapelessRecipe.create(id, new CustomPacketBuffer(pBuffer));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, BaseShapelessRecipe recipe) {
        recipe.toNetwork(buf);
    }
}
