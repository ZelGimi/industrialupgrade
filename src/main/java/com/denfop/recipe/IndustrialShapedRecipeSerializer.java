package com.denfop.recipe;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseRecipe;
import com.denfop.api.crafting.PartRecipe;
import com.denfop.api.crafting.RecipeGrid;
import com.denfop.network.packet.CustomPacketBuffer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndustrialShapedRecipeSerializer implements RecipeSerializer<BaseRecipe> {
    public static final MapCodec<BaseRecipe> CODEC = RecordCodecBuilder.mapCodec((p_340778_) -> p_340778_.group(Codec.STRING.optionalFieldOf("group", "").forGetter(BaseRecipe::getGroup), CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(BaseRecipe::category), ShapedRecipePattern.MAP_CODEC.forGetter(BaseRecipe::getPattern), ItemStack.STRICT_CODEC.fieldOf("result").forGetter(BaseRecipe::getOutput), Codec.BOOL.optionalFieldOf("show_notification", true).forGetter((p_311731_) -> false)).apply(p_340778_, (group, category, pattern, result, show) -> {
        ShapedRecipePattern.Data data = (pattern).getData().orElseGet(() -> new ShapedRecipePattern.Data(new HashMap<>(), new ArrayList<>()));
        RecipeGrid grid = new RecipeGrid(new ArrayList<>(data.pattern()));
        List<PartRecipe> partRecipes = new ArrayList<>();

        for (Map.Entry<Character, Ingredient> entry : data.key().entrySet()) {


            Ingredient ingredient = entry.getValue();
            IInputItemStack input = Recipes.inputFactory.getInput(ingredient);

            partRecipes.add(new PartRecipe(String.valueOf(entry.getKey()), input));
        }
        return new BaseRecipe(group, category, result, grid, partRecipes);

    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, BaseRecipe> STREAM_CODEC = StreamCodec.of(IndustrialShapedRecipeSerializer::toNetwork, IndustrialShapedRecipeSerializer::fromNetwork);


    public IndustrialShapedRecipeSerializer() {
    }

    private static BaseRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
        return BaseRecipe.create(new CustomPacketBuffer(pBuffer));
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, BaseRecipe p_320586_) {
        p_320586_.toNetwork(p_320738_);
    }

    public MapCodec<BaseRecipe> codec() {
        return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, BaseRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
