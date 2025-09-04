package com.denfop.recipe.universalrecipe;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blockentity.smeltery.BlockEntitySmelteryController;
import com.denfop.blockentity.smeltery.BlockEntitySmelteryFurnace;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SmelterSerializer implements RecipeSerializer<SmelteryRecipe> {
    public static final SmelterSerializer INSTANCE = new SmelterSerializer();
    public static List<ResourceLocation> resourceLocations = new LinkedList<>();

    @Override
    public SmelteryRecipe fromJson(ResourceLocation id, JsonObject json) {
        String operation = GsonHelper.getAsString(json, "operation");
        if (resourceLocations.contains(id))
            return new SmelteryRecipe(id, "", Collections.emptyList(), ItemStack.EMPTY);
        switch (operation) {
            case "furnace":
                ItemStack stack = null;
                if (GsonHelper.isValidNode(json, "inputs")) {
                    JsonArray inArray = GsonHelper.getAsJsonArray(json, "inputs");
                    for (JsonElement el : inArray) {
                        JsonObject obj = el.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(obj, "id");
                        int amount = GsonHelper.getAsInt(obj, "amount", 1);
                        stack = new ItemStack(
                                ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)),
                                amount
                        );
                        break;
                    }

                }
                FluidStack fluidStack = null;
                if (GsonHelper.isValidNode(json, "outputs")) {
                    JsonArray inArray = GsonHelper.getAsJsonArray(json, "outputs");
                    for (JsonElement el : inArray) {
                        JsonObject obj = el.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(obj, "id");
                        int amount = GsonHelper.getAsInt(obj, "amount", 1);
                        fluidStack = new FluidStack(
                                ForgeRegistries.FLUIDS.getValue(new ResourceLocation(itemId)),
                                amount
                        );
                        break;
                    }
                }
                if (stack != null && fluidStack != null)
                    BlockEntitySmelteryFurnace.addRecipe("", stack, fluidStack);
                break;
            case "castings_ingot":
                stack = null;

                if (GsonHelper.isValidNode(json, "outputs")) {
                    JsonArray inArray = GsonHelper.getAsJsonArray(json, "outputs");
                    for (JsonElement el : inArray) {
                        JsonObject obj = el.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(obj, "id");
                        int amount = GsonHelper.getAsInt(obj, "amount", 1);
                        stack = new ItemStack(
                                ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(itemId)),
                                amount
                        );
                        break;
                    }
                }
                fluidStack = null;
                if (GsonHelper.isValidNode(json, "inputs")) {
                    JsonArray inArray = GsonHelper.getAsJsonArray(json, "inputs");
                    for (JsonElement el : inArray) {
                        JsonObject obj = el.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(obj, "id");
                        int amount = GsonHelper.getAsInt(obj, "amount", 1);
                        fluidStack = new FluidStack(
                                ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(itemId)),
                                amount
                        );
                        break;
                    }

                }
                Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                        fluidStack), new RecipeOutput(
                        null,
                        stack
                )));
                break;
            case "castings_gear":
                stack = null;

                if (GsonHelper.isValidNode(json, "outputs")) {
                    JsonArray inArray = GsonHelper.getAsJsonArray(json, "outputs");
                    for (JsonElement el : inArray) {
                        JsonObject obj = el.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(obj, "id");
                        int amount = GsonHelper.getAsInt(obj, "amount", 1);
                        stack = new ItemStack(
                                ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(itemId)),
                                amount
                        );
                        break;
                    }

                }
                fluidStack = null;
                if (GsonHelper.isValidNode(json, "inputs")) {
                    JsonArray inArray = GsonHelper.getAsJsonArray(json, "inputs");
                    for (JsonElement el : inArray) {
                        JsonObject obj = el.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(obj, "id");
                        int amount = GsonHelper.getAsInt(obj, "amount", 1);
                        fluidStack = new FluidStack(
                                ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(itemId)),
                                amount
                        );
                        break;
                    }

                }
                Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                        fluidStack), new RecipeOutput(
                        null,
                        stack
                )));
                break;
            case "mix":
                FluidStack fluidStack1 = null;

                if (GsonHelper.isValidNode(json, "outputs")) {
                    JsonArray inArray = GsonHelper.getAsJsonArray(json, "outputs");
                    for (JsonElement el : inArray) {
                        JsonObject obj = el.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(obj, "id");
                        int amount = GsonHelper.getAsInt(obj, "amount", 1);
                        fluidStack1 = new FluidStack(
                                ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(itemId)),
                                amount
                        );
                        break;
                    }

                }
                List<FluidStack> list = new LinkedList<>();
                if (GsonHelper.isValidNode(json, "inputs")) {
                    JsonArray inArray = GsonHelper.getAsJsonArray(json, "inputs");
                    for (JsonElement el : inArray) {
                        JsonObject obj = el.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(obj, "id");
                        int amount = GsonHelper.getAsInt(obj, "amount", 1);
                        fluidStack = new FluidStack(
                                ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(itemId)),
                                amount
                        );
                        list.add(fluidStack);
                    }

                }
                BlockEntitySmelteryController.mapRecipes.put(list, fluidStack1);
                break;
        }


        return new SmelteryRecipe(id, "", Collections.emptyList(), ItemStack.EMPTY);
    }

    @Override
    public SmelteryRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        return new SmelteryRecipe(id, "", new ArrayList<>(), ItemStack.EMPTY);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SmelteryRecipe recipe) {


    }
}
