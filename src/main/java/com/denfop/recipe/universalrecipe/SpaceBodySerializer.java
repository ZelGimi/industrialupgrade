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
        IBody body = SpaceNet.instance.getBodyFromName(bodyName.toLowerCase());
        if (body != null)
            if (operationType.equals("add")) {
                switch (roverType) {
                    case "rover":
                        for (IInputItemStack itemStack : input) {
                            if (itemStack instanceof InputItemStack)
                                regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, body, EnumTypeRovers.ROVERS));
                            if (itemStack instanceof InputFluidStack)
                                regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, body, EnumTypeRovers.ROVERS));
                            if (itemStack instanceof InputOreDict)
                                regBaseResource.add(() -> new BaseResource(itemStack.getInputs().get(0), chance, 100, percent, body, EnumTypeRovers.ROVERS));

                        }
                        break;
                    case "probe":
                        for (IInputItemStack itemStack : input) {
                            if (itemStack instanceof InputItemStack)
                                regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, body, EnumTypeRovers.PROBE));
                            if (itemStack instanceof InputFluidStack)
                                regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, body, EnumTypeRovers.PROBE));
                            if (itemStack instanceof InputOreDict)
                                regBaseResource.add(() -> new BaseResource(((InputOreDict) itemStack).getInputs().get(0), chance, 100, percent, body, EnumTypeRovers.PROBE));

                        }
                        break;
                    case "satellite":
                        for (IInputItemStack itemStack : input) {
                            if (itemStack instanceof InputItemStack)
                                regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, body, EnumTypeRovers.SATELLITE));
                            if (itemStack instanceof InputFluidStack)
                                regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, body, EnumTypeRovers.SATELLITE));
                            if (itemStack instanceof InputOreDict)
                                regBaseResource.add(() -> new BaseResource(itemStack.getInputs().get(0), chance, 100, percent, body, EnumTypeRovers.SATELLITE));

                        }
                        break;
                    case "rocket":
                        for (IInputItemStack itemStack : input) {
                            if (itemStack instanceof InputItemStack)
                                regBaseResource.add(() -> new BaseResource(((InputItemStack) itemStack).input, chance, 100, percent, body, EnumTypeRovers.ROCKET));
                            if (itemStack instanceof InputFluidStack)
                                regBaseResource.add(() -> new BaseResource(((InputFluidStack) itemStack).getFluid(), chance, 100, percent, body, EnumTypeRovers.ROCKET));
                            if (itemStack instanceof InputOreDict)
                                regBaseResource.add(() -> new BaseResource(((InputOreDict) itemStack).getInputs().get(0), chance, 100, percent, body, EnumTypeRovers.ROCKET));

                        }
                        break;
                }
            }


        return new SpaceBodyRecipe(id, "", Collections.emptyList(), "");
    }

    @Override
    public SpaceBodyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        return new SpaceBodyRecipe(id, "", new ArrayList<>(), "");
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SpaceBodyRecipe recipe) {


    }
}
