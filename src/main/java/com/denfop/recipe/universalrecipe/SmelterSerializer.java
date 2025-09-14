package com.denfop.recipe.universalrecipe;

import com.denfop.IUCore;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blockentity.smeltery.BlockEntitySmelteryController;
import com.denfop.blockentity.smeltery.BlockEntitySmelteryFurnace;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputFluidStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipe.InputOreDict;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
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
            return new SmelteryRecipe(id, "", new LinkedList<>(), new LinkedList<>());
        List<IInputItemStack> inputs = new ArrayList<>();
        List<IInputItemStack> outputs = new ArrayList<>();
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
                        inputs.add(new InputItemStack(stack));
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
                        outputs.add(new InputFluidStack(fluidStack));
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
                        outputs.add(new InputItemStack(stack));
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
                        inputs.add(new InputFluidStack(fluidStack));
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
                        outputs.add(new InputItemStack(stack));
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
                        inputs.add(new InputFluidStack(fluidStack));
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

                        outputs.add(new InputFluidStack(fluidStack1));
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
                        inputs.add(new InputFluidStack(fluidStack));
                    }

                }
                BlockEntitySmelteryController.mapRecipes.put(list, fluidStack1);
                break;
        }


        return new SmelteryRecipe(id, operation, inputs, outputs);
    }

    @Override
    public SmelteryRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        String operation = buf.readUtf();
        int inSize = buf.readVarInt();
        List<IInputItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < inSize; i++) {
            String type = buf.readUtf();
            switch (type) {
                case "fluid":
                    ResourceLocation fluidId = buf.readResourceLocation();
                    int amountF = buf.readVarInt();
                    inputs.add(new InputFluidStack(new FluidStack(Registry.FLUID.get(fluidId), amountF)));
                    break;
                case "tag":
                    String tag = buf.readUtf();
                    int amountT = buf.readVarInt();
                    inputs.add(new InputOreDict(tag, amountT));
                    break;
                case "item":
                    ResourceLocation itemId = buf.readResourceLocation();
                    int amountI = buf.readVarInt();
                    inputs.add(new InputItemStack(new ItemStack(Registry.ITEM.get(itemId), amountI)));
                    break;
            }
        }
        int outSize = buf.readVarInt();
        List<IInputItemStack> outputs = new ArrayList<>();
        for (int i = 0; i < outSize; i++) {
            String type = buf.readUtf();
            switch (type) {
                case "fluid":
                    ResourceLocation fluidId = buf.readResourceLocation();
                    int amountF = buf.readVarInt();
                    outputs.add(new InputFluidStack(new FluidStack(Registry.FLUID.get(fluidId), amountF)));
                    break;
                case "tag":
                    String tag = buf.readUtf();
                    int amountT = buf.readVarInt();
                    outputs.add(new InputOreDict(tag, amountT));
                    break;
                case "item":
                    ResourceLocation itemId = buf.readResourceLocation();
                    int amountI = buf.readVarInt();
                    inputs.add(new InputItemStack(new ItemStack(Registry.ITEM.get(itemId), amountI)));
                    break;
            }
        }
        if (!IUCore.updateRecipe){
            List<FluidStack> fluidStacksInput = new ArrayList<>();
            List<ItemStack> itemStacksInput = new ArrayList<>();
            List<FluidStack> fluidStacksOutput = new ArrayList<>();
            List<ItemStack> itemStacksOutput = new ArrayList<>();
            for (IInputItemStack o : inputs) {
                if (o instanceof InputFluidStack)
                    fluidStacksInput.add(((InputFluidStack) o).getFluid());
                else
                    itemStacksInput.add((o).getInputs().get(0));
            }
            for (IInputItemStack o : outputs) {
                if (o instanceof InputFluidStack)
                    fluidStacksOutput.add(((InputFluidStack) o).getFluid());
                else
                    itemStacksOutput.add((o).getInputs().get(0));
            }
            switch (operation) {
                case "furnace":
                    ItemStack stack = itemStacksInput.get(0);
                    FluidStack fluidStack = fluidStacksOutput.get(0);
                    if (stack != null && fluidStack != null)
                        BlockEntitySmelteryFurnace.addRecipe("", stack, fluidStack);
                    break;
                case "castings_ingot":
                    stack = itemStacksOutput.get(0);
                    fluidStack = fluidStacksInput.get(0);
                    Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                            fluidStack), new RecipeOutput(
                            null,
                            stack
                    )));
                    break;
                case "castings_gear":
                    stack = itemStacksOutput.get(0);

                    fluidStack = fluidStacksInput.get(0);
                    Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                            fluidStack), new RecipeOutput(
                            null,
                            stack
                    )));
                    break;
                case "mix":
                    FluidStack fluidStack1 = fluidStacksOutput.get(0);


                    List<FluidStack> list = fluidStacksInput;
                    BlockEntitySmelteryController.mapRecipes.put(list, fluidStack1);
                    break;
            }
        }
        return new SmelteryRecipe(id,operation, inputs, outputs);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SmelteryRecipe recipe) {
        buf.writeUtf(recipe.getOperation());
        buf.writeVarInt(recipe.getInputs().size());
        for (IInputItemStack input : recipe.getInputs()) {
            if (input instanceof InputFluidStack) {
                buf.writeUtf("fluid");
                buf.writeResourceLocation(Registry.FLUID.getKey(((InputFluidStack) input).getFluid().getFluid()));
                buf.writeVarInt(((InputFluidStack) input).getFluid().getAmount());
            } else if (input instanceof InputOreDict) {
                buf.writeUtf("tag");
                buf.writeUtf(input.getTag().location().toString());
                buf.writeVarInt(input.getAmount());
            } else if (input instanceof InputItemStack) {
                buf.writeUtf("item");
                buf.writeResourceLocation(Registry.ITEM.getKey(((InputItemStack) input).input.getItem()));
                buf.writeVarInt(input.getAmount());
            }
        }


        buf.writeVarInt(recipe.getOutputs().size());
        for (IInputItemStack output : recipe.getOutputs()) {
            if (output instanceof InputFluidStack) {
                buf.writeUtf("fluid");
                buf.writeResourceLocation(Registry.FLUID.getKey(((InputFluidStack) output).getFluid().getFluid()));
                buf.writeVarInt(((InputFluidStack) output).getFluid().getAmount());
            } else if (output instanceof InputOreDict) {
                buf.writeUtf("tag");
                buf.writeUtf(output.getTag().location().toString());
                buf.writeVarInt(output.getAmount());
            } else if (output instanceof InputItemStack) {
                buf.writeUtf("item");
                buf.writeResourceLocation(Registry.ITEM.getKey(((InputItemStack) output).input.getItem()));
                buf.writeVarInt(output.getAmount());
            }
        }

    }
}
