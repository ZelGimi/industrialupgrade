package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.BaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputFluidStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipe.InputOreDict;
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
import java.util.List;

import static com.denfop.IUCore.register;
import static com.denfop.IUCore.updateRecipe;
import static com.denfop.api.space.SpaceInit.regBaseResource;

public class SpaceBodySerializer implements RecipeSerializer<SpaceBodyRecipe> {
    public static final SpaceBodySerializer INSTANCE = new SpaceBodySerializer();

    @Override
    public SpaceBodyRecipe fromJson(ResourceLocation id, JsonObject json) {
        String bodyName = GsonHelper.getAsString(json, "body");

        List<IInputItemStack> input = new ArrayList<>();
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
                        input.add(new InputItemStack(stack));
                        break;
                    case "tag":
                        input.add(new InputOreDict(itemId, amount));
                        break;
                    case "fluid":
                        FluidStack fluidStack = new FluidStack(
                                ForgeRegistries.FLUIDS.getValue(new ResourceLocation(itemId)),
                                amount
                        );
                        input.add(new InputFluidStack(fluidStack));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown input type: " + type);
                }
            }
        }
        String roverType = GsonHelper.getAsString(json, "typeRover");
        String operationType = GsonHelper.getAsString(json, "typeOperation");
        int percent = GsonHelper.getAsInt(json, "percent");
        int chance = GsonHelper.getAsInt(json, "chance");

        if (operationType.equals("addAll")) {
            switch (roverType) {
                case "rover":
                    for (IInputItemStack itemStack : input) {
                        if (itemStack instanceof InputItemStack)
                            regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROVERS));
                        if (itemStack instanceof InputFluidStack)
                            regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROVERS));
                        if (itemStack instanceof InputOreDict)
                            regBaseResource.add(() -> new BaseResource(itemStack.getInputs().get(0), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROVERS));

                    }
                    break;
                case "probe":
                    for (IInputItemStack itemStack : input) {
                        if (itemStack instanceof InputItemStack)
                            regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.PROBE));
                        if (itemStack instanceof InputFluidStack)
                            regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.PROBE));
                        if (itemStack instanceof InputOreDict)
                            regBaseResource.add(() -> new BaseResource(((InputOreDict) itemStack).getInputs().get(0), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.PROBE));

                    }
                    break;
                case "satellite":
                    for (IInputItemStack itemStack : input) {
                        if (itemStack instanceof InputItemStack)
                            regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.SATELLITE));
                        if (itemStack instanceof InputFluidStack)
                            regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.SATELLITE));
                        if (itemStack instanceof InputOreDict)
                            regBaseResource.add(() -> new BaseResource(itemStack.getInputs().get(0), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.SATELLITE));

                    }
                    break;
                case "rocket":
                    for (IInputItemStack itemStack : input) {
                        if (itemStack instanceof InputItemStack)
                            regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROCKET));
                        if (itemStack instanceof InputFluidStack)
                            regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROCKET));
                        if (itemStack instanceof InputOreDict)
                            regBaseResource.add(() -> new BaseResource(((InputOreDict) itemStack).getInputs().get(0), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROCKET));

                    }
                    break;
            }
        }


        return new SpaceBodyRecipe(id, bodyName, percent, chance, roverType, operationType, input);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SpaceBodyRecipe recipe) {
        buf.writeUtf(recipe.bodyName);
        buf.writeVarInt(recipe.percent);
        buf.writeVarInt(recipe.chance);
        buf.writeUtf(recipe.roverType);
        buf.writeUtf(recipe.operationType);

        buf.writeVarInt(recipe.input.size());
        for (IInputItemStack inputStack : recipe.input) {
            if (inputStack instanceof InputItemStack stack) {
                buf.writeUtf("item");
                buf.writeUtf(ForgeRegistries.ITEMS.getKey(stack.input.getItem()).toString());
                buf.writeVarInt(stack.input.getCount());
            } else if (inputStack instanceof InputFluidStack fluid) {
                buf.writeUtf("fluid");
                buf.writeUtf(ForgeRegistries.FLUIDS.getKey(fluid.getFluid().getFluid()).toString());
                buf.writeVarInt(fluid.getFluid().getAmount());
            } else if (inputStack instanceof InputOreDict ore) {
                buf.writeUtf("tag");
                buf.writeUtf(ore.getTag().location().toString());
                buf.writeVarInt(ore.getInputs().get(0).getCount());
            } else {
                throw new IllegalArgumentException("Unknown input type: " + inputStack);
            }
        }
    }

    @Override
    public SpaceBodyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String bodyName = buf.readUtf();
        int percent = buf.readVarInt();
        int chance = buf.readVarInt();
        String typeRover = buf.readUtf();
        String typeOperation = buf.readUtf();

        int size = buf.readVarInt();
        List<IInputItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String type = buf.readUtf();
            switch (type) {
                case "item" -> {
                    ResourceLocation itemId = buf.readResourceLocation();
                    int amount = buf.readVarInt();
                    ItemStack itemStack = new ItemStack(
                            ForgeRegistries.ITEMS.getValue(itemId),
                            amount
                    );
                    inputs.add(new InputItemStack(itemStack));
                }
                case "fluid" -> {
                    ResourceLocation fluidId = buf.readResourceLocation();
                    int amount = buf.readVarInt();
                    FluidStack fluidStack = new FluidStack(
                            ForgeRegistries.FLUIDS.getValue(fluidId),
                            amount
                    );
                    inputs.add(new InputFluidStack(fluidStack));
                }
                case "tag" -> {
                    String tagId = buf.readUtf();
                    int amount = buf.readVarInt();
                    inputs.add(new InputOreDict(tagId, amount));
                }
                default -> throw new IllegalArgumentException("Unknown input type: " + type);
            }
        }
        if (!updateRecipe)
            if (typeRover.equals("add")) {
                switch (typeOperation) {
                    case "rover":
                        for (IInputItemStack itemStack : inputs) {
                            if (itemStack instanceof InputItemStack)
                                regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROVERS));
                            if (itemStack instanceof InputFluidStack)
                                regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROVERS));
                            if (itemStack instanceof InputOreDict)
                                regBaseResource.add(() -> new BaseResource(itemStack.getInputs().get(0), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROVERS));

                        }
                        break;
                    case "probe":
                        for (IInputItemStack itemStack : inputs) {
                            if (itemStack instanceof InputItemStack)
                                regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.PROBE));
                            if (itemStack instanceof InputFluidStack)
                                regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.PROBE));
                            if (itemStack instanceof InputOreDict)
                                regBaseResource.add(() -> new BaseResource(((InputOreDict) itemStack).getInputs().get(0), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.PROBE));

                        }
                        break;
                    case "satellite":
                        for (IInputItemStack itemStack : inputs) {
                            if (itemStack instanceof InputItemStack)
                                regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.SATELLITE));
                            if (itemStack instanceof InputFluidStack)
                                regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.SATELLITE));
                            if (itemStack instanceof InputOreDict)
                                regBaseResource.add(() -> new BaseResource(itemStack.getInputs().get(0), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.SATELLITE));

                        }
                        break;
                    case "rocket":
                        for (IInputItemStack itemStack : inputs) {
                            if (itemStack instanceof InputItemStack)
                                regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROCKET));
                            if (itemStack instanceof InputFluidStack)
                                regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROCKET));
                            if (itemStack instanceof InputOreDict)
                                regBaseResource.add(() -> new BaseResource(((InputOreDict) itemStack).getInputs().get(0), chance, 100, percent, SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), EnumTypeRovers.ROCKET));

                        }
                        break;
                }
            }
        return new SpaceBodyRecipe(id, bodyName, percent, chance, typeRover, typeOperation, inputs);
    }
}
