package com.denfop.recipe.universalrecipe;

import com.denfop.api.Recipes;
import com.denfop.recipe.InputItemStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class IURecipeDeleteSerializer implements RecipeSerializer<IURecipeDelete> {
    public static final IURecipeDeleteSerializer INSTANCE = new IURecipeDeleteSerializer();

    @Override
    public IURecipeDelete fromJson(ResourceLocation id, JsonObject json) {
        String recipeType = GsonHelper.getAsString(json, "recipe_type");
        boolean isFluidRecipe = GsonHelper.getAsBoolean(json, "isFluidRecipe", false);
        boolean removeAll = GsonHelper.getAsBoolean(json, "isRemoveAll", false);

        List<ItemStack> outputs1 = new ArrayList<>();
        List<FluidStack> outputsFluid = new ArrayList<>();

        JsonArray outArr = GsonHelper.getAsJsonArray(json, "output");
        for (JsonElement el : outArr) {
            JsonObject obj = el.getAsJsonObject();
            String itemId = GsonHelper.getAsString(obj, "id");
            String type = GsonHelper.getAsString(obj, "type");
            int amount = GsonHelper.getAsInt(obj, "amount", 1);
            ResourceLocation location = new ResourceLocation(itemId);

            switch (type) {
                case "item": {
                    Item item = ForgeRegistries.ITEMS.getValue(location);
                    if (item == null) {
                        break;
                    }

                    ItemStack stack = new ItemStack(item, amount);
                    if (!stack.isEmpty()) {
                        outputs1.add(new InputItemStack(stack).input);
                    }
                    break;
                }
                case "fluid": {
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue(location);
                    if (fluid == null) {
                        break;
                    }

                    FluidStack fluidStack = new FluidStack(fluid, amount);
                    if (!fluidStack.isEmpty()) {
                        outputsFluid.add(fluidStack);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown output type: " + type);
            }
        }

        if (isFluidRecipe) {
            if (!outputsFluid.isEmpty()) {
                Recipes.recipes.addFluidRemoveRecipe(recipeType, outputsFluid.get(0), removeAll);
            } else if (!outputs1.isEmpty()) {
                Recipes.recipes.addFluidItemRemoveRecipe(recipeType, outputs1.get(0), removeAll);
            }
        } else {
            if (!outputs1.isEmpty()) {
                Recipes.recipes.addRemoveRecipe(recipeType, outputs1.get(0), removeAll);
            }
        }

        return new IURecipeDelete(id, recipeType, isFluidRecipe, new ArrayList<>(), removeAll);
    }

    @Override
    public IURecipeDelete fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return new IURecipeDelete(id, "", false, new ArrayList<>(), false);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, IURecipeDelete recipe) {

    }
}
