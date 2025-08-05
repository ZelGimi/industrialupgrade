package com.denfop.recipe;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseShapelessRecipe;
import com.denfop.network.packet.CustomPacketBuffer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public class IndustrialShapelessRecipeSerializer implements RecipeSerializer<BaseShapelessRecipe> {
    public static final StreamCodec<RegistryFriendlyByteBuf, BaseShapelessRecipe> STREAM_CODEC = StreamCodec.of(IndustrialShapelessRecipeSerializer::toNetwork, IndustrialShapelessRecipeSerializer::fromNetwork);
    private static final MapCodec<BaseShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec((p_340779_) -> {
        return p_340779_.group(Codec.STRING.optionalFieldOf("group", "").forGetter(Recipe::getGroup), CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(BaseShapelessRecipe::category), ItemStack.STRICT_CODEC.fieldOf("result").forGetter(BaseShapelessRecipe::getOutput), Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap((p_301021_) -> {
            Ingredient[] aingredient = p_301021_.toArray(Ingredient[]::new);
            if (aingredient.length == 0) {
                return DataResult.error(() -> "No ingredients for shapeless recipe");
            } else {
                return aingredient.length > 3 * 3 ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(3 * 3)) : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
            }
        }, DataResult::success).forGetter(BaseShapelessRecipe::getIngredients)).apply(p_340779_, (group, category, result, ingredient) -> {
            List<IInputItemStack> partRecipes = new ArrayList<>();

            for (Ingredient entry : ingredient) {


                Ingredient ingredient1 = entry;
                IInputItemStack input = Recipes.inputFactory.getInput(ingredient1);

                partRecipes.add(input);
            }
            return new BaseShapelessRecipe(result, partRecipes);
        });
    });


    public IndustrialShapelessRecipeSerializer() {
    }

    private static BaseShapelessRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
        return BaseShapelessRecipe.create(new CustomPacketBuffer(pBuffer));
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, BaseShapelessRecipe p_320586_) {
        p_320586_.toNetwork(p_320738_);
    }

    public MapCodec<BaseShapelessRecipe> codec() {
        return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, BaseShapelessRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
