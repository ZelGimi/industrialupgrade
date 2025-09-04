package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.IBody;
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
import java.util.Collections;
import java.util.List;

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
        IBody body = SpaceNet.instance.getBodyFromName(bodyName.toLowerCase());
        for (IInputItemStack itemStack : input) {
            if (itemStack instanceof InputItemStack)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addItemStack(body, (short) level, ((InputItemStack) itemStack).input));
            if (itemStack instanceof InputFluidStack)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addFluidStack(body, (short) level, ((InputFluidStack) itemStack).getFluid()));
            if (itemStack instanceof InputOreDict)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addItemStack(body, (short) level, ((InputOreDict) itemStack).getInputs().get(0)));

        }

        return new ColonyRecipe(id, "", Collections.emptyList(), "");
    }

    @Override
    public ColonyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        return new ColonyRecipe(id, "", new ArrayList<>(), "");
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, ColonyRecipe recipe) {


    }
}
