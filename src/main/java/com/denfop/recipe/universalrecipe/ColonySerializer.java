package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.SpaceNet;
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
import java.util.List;

import static com.denfop.IUCore.register;
import static com.denfop.IUCore.updateRecipe;
import static com.denfop.api.space.SpaceInit.regColonyBaseResource;

public class ColonySerializer implements RecipeSerializer<ColonyRecipe> {
    public static final ColonySerializer INSTANCE = new ColonySerializer();

    @Override
    public ColonyRecipe fromJson(ResourceLocation id, JsonObject json) {
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

        int level = GsonHelper.getAsInt(json, "level");
        for (IInputItemStack itemStack : input) {
            if (itemStack instanceof InputItemStack)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addItemStack(SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), (short) level, ((InputItemStack) itemStack).input));
            if (itemStack instanceof InputFluidStack)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addFluidStack(SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), (short) level, ((InputFluidStack) itemStack).getFluid()));
            if (itemStack instanceof InputOreDict)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addItemStack(SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), (short) level, ((InputOreDict) itemStack).getInputs().get(0)));

        }

        return new ColonyRecipe(id, bodyName, input, level);
    }

    @Override
    public ColonyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        String bodyName = buf.readUtf();
        int level = buf.readVarInt();
        int inputSize = buf.readVarInt();
        List<IInputItemStack> input = new ArrayList<>();

        for (int i = 0; i < inputSize; i++) {
            String type = buf.readUtf();
            String itemId = buf.readUtf();
            int amount = buf.readVarInt();

            switch (type) {
                case "item":
                    input.add(new InputItemStack(new ItemStack(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)),
                            amount
                    )));
                    break;
                case "fluid":
                    input.add(new InputFluidStack(new FluidStack(
                            ForgeRegistries.FLUIDS.getValue(new ResourceLocation(itemId)),
                            amount
                    )));
                    break;
                case "tag":
                    input.add(new InputOreDict(itemId, amount));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown input type: " + type);
            }
        }
        if (!updateRecipe) {
            for (IInputItemStack itemStack : input) {
                if (itemStack instanceof InputItemStack)
                    regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addItemStack(SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), (short) level, ((InputItemStack) itemStack).input));
                if (itemStack instanceof InputFluidStack)
                    regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addFluidStack(SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), (short) level, ((InputFluidStack) itemStack).getFluid()));
                if (itemStack instanceof InputOreDict)
                    regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addItemStack(SpaceNet.instance.getBodyFromName(bodyName.toLowerCase()), (short) level, ((InputOreDict) itemStack).getInputs().get(0)));

            }
        }
        return new ColonyRecipe(id, bodyName, input, level);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, ColonyRecipe recipe) {
        buf.writeUtf(recipe.bodyName);
        buf.writeVarInt(recipe.level);
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
}
