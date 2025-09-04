package com.denfop.recipe.universalrecipe;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputFluidStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipe.InputOreDict;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IURecipeSerializer implements RecipeSerializer<IURecipe> {
    public static final IURecipeSerializer INSTANCE = new IURecipeSerializer();

    @Override
    public IURecipe fromJson(ResourceLocation id, JsonObject json) {
        String recipeType = GsonHelper.getAsString(json, "recipe_type");
        boolean isFluidRecipe = GsonHelper.getAsBoolean(json, "isFluidRecipe", false);

        List<IInputItemStack> inputs = new ArrayList<>();
        List<FluidStack> fluidStacks = new ArrayList<>();
        if (GsonHelper.isValidNode(json, "inputs")) {
            JsonArray inArray = GsonHelper.getAsJsonArray(json, "inputs");
            for (JsonElement el : inArray) {
                JsonObject obj = el.getAsJsonObject();
                String type = GsonHelper.getAsString(obj, "type");
                String itemId = GsonHelper.getAsString(obj, "id");
                int amount = GsonHelper.getAsInt(obj, "amount", 1);

                switch (type) {
                    case "item":
                        ItemStack stack = new ItemStack(
                                ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)),
                                amount
                        );
                        inputs.add(new InputItemStack(stack));
                        break;

                    case "fluid":
                        FluidStack fluidStack = new FluidStack(
                                ForgeRegistries.FLUIDS.getValue(new ResourceLocation(itemId)),
                                amount
                        );
                        fluidStacks.add(fluidStack);
                        break;

                    case "tag":
                        inputs.add(new InputOreDict(itemId, amount));
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown input type: " + type);
                }
            }
        }

        List<IInputItemStack> outputs = new ArrayList<>();
        List<FluidStack> outputsFluid = new ArrayList<>();
        JsonArray outArr = GsonHelper.getAsJsonArray(json, "outputs");
        for (JsonElement el : outArr) {
            JsonObject obj = el.getAsJsonObject();
            String itemId = GsonHelper.getAsString(obj, "id");
            String type = GsonHelper.getAsString(obj, "type");
            int amount = GsonHelper.getAsInt(obj, "amount", 1);
            switch (type) {
                case "item":
                    ItemStack stack = new ItemStack(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)),
                            amount
                    );
                    outputs.add(new InputItemStack(stack));
                    break;

                case "fluid":
                    FluidStack fluidStack = new FluidStack(
                            ForgeRegistries.FLUIDS.getValue(new ResourceLocation(itemId)),
                            amount
                    );
                    outputsFluid.add(fluidStack);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown input type: " + type);
            }
        }

        Map<String, Object> params = new HashMap<>();
        if (json.has("params")) {
            JsonObject pJson = json.getAsJsonObject("params");
            for (String key : pJson.keySet()) {
                JsonElement val = pJson.get(key);
                if (val.isJsonPrimitive()) {
                    if (val.getAsJsonPrimitive().isBoolean()) {
                        params.put(key, val.getAsBoolean());
                    } else if (val.getAsJsonPrimitive().isNumber()) {
                        params.put(key, val.getAsNumber());
                    } else {
                        params.put(key, val.getAsString());
                    }
                }
            }
        }
        CompoundTag compoundTag = new CompoundTag();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof Boolean) {
                compoundTag.putBoolean(entry.getKey(), (Boolean) entry.getValue());
            }
            if (entry.getValue() instanceof Number) {
                compoundTag.putDouble(entry.getKey(), ((Number) entry.getValue()).doubleValue());
            }
        }
        List<ItemStack> outputs1 = new ArrayList<>();
        outputs.forEach(outputs2 -> {
            if (!(outputs2 instanceof InputFluidStack)) {
                outputs1.add(outputs2.getInputs().get(0));
            }
        });
        if (!inputs.isEmpty() && fluidStacks.isEmpty()) {

            Recipes.recipes.addAdderRecipe(recipeType, new BaseMachineRecipe(new Input(inputs), new RecipeOutput(compoundTag, outputs1)));
        } else if (!inputs.isEmpty() && !isFluidRecipe) {
            Recipes.recipes.addAdderRecipe(recipeType, new BaseMachineRecipe(new Input(fluidStacks.get(0), inputs), new RecipeOutput(compoundTag, outputs1)));

        } else if (inputs.isEmpty() && isFluidRecipe) {
            if (!outputs1.isEmpty() && outputsFluid.isEmpty()) {
                Recipes.recipes.addFluidAdderRecipe(recipeType, new BaseFluidMachineRecipe(new InputFluid(fluidStacks.toArray(new FluidStack[0])), new RecipeOutput(compoundTag, outputs1)));
            } else if (!outputs1.isEmpty() && !outputsFluid.isEmpty()) {
                Recipes.recipes.addFluidAdderRecipe(recipeType, new BaseFluidMachineRecipe(new InputFluid(fluidStacks.toArray(new FluidStack[0])), new RecipeOutput(compoundTag, outputs1), outputsFluid));

            } else if (outputs1.isEmpty() && !outputsFluid.isEmpty()) {
                Recipes.recipes.addFluidAdderRecipe(recipeType, new BaseFluidMachineRecipe(new InputFluid(fluidStacks.toArray(new FluidStack[0])), outputsFluid));

            }
        } else if (!inputs.isEmpty() && isFluidRecipe) {
            Recipes.recipes.addAdderRecipe(recipeType, new BaseMachineRecipe(new Input(fluidStacks.get(0), inputs), new RecipeOutput(compoundTag, outputs1)));
            if (!outputs1.isEmpty() && outputsFluid.isEmpty()) {
                Recipes.recipes.addFluidAdderRecipe(recipeType, new BaseFluidMachineRecipe(new InputFluid(inputs.get(0).getInputs().get(0), fluidStacks.toArray(new FluidStack[0])), new RecipeOutput(compoundTag, outputs1)));
            } else if (!outputs1.isEmpty() && !outputsFluid.isEmpty()) {
                Recipes.recipes.addFluidAdderRecipe(recipeType, new BaseFluidMachineRecipe(new InputFluid(inputs.get(0).getInputs().get(0), fluidStacks.toArray(new FluidStack[0])), new RecipeOutput(compoundTag, outputs1), outputsFluid));

            } else if (outputs1.isEmpty() && !outputsFluid.isEmpty()) {
                Recipes.recipes.addFluidAdderRecipe(recipeType, new BaseFluidMachineRecipe(new InputFluid(inputs.get(0).getInputs().get(0), fluidStacks.toArray(new FluidStack[0])), outputsFluid));

            }
        }
        return new IURecipe(id, recipeType, inputs, outputs1, params);
    }

    @Override
    public IURecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        return new IURecipe(id, "", new ArrayList<>(), new ArrayList<>(), new HashMap<>());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, IURecipe recipe) {


    }
}
