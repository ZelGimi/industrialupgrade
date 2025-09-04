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
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SmelterSerializer implements RecipeSerializer<SmelteryRecipe> {
    public static final MapCodec<SmelteryRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> {

        Codec<IInputItemStack> singleInputCodec = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("type").forGetter(i -> i instanceof InputFluidStack ? "fluid" : "item"),
                ResourceLocation.CODEC.fieldOf("id").forGetter(i -> BuiltInRegistries.ITEM.getKey(i.getInputs().get(0).getItem())),
                Codec.INT.fieldOf("amount").orElse(1).forGetter(i -> i.getInputs().get(0).getCount())
        ).apply(inst, (type, id, amt) -> {
            if ("tag".equals(type))
                return new InputOreDict(id.getNamespace() + ":" + id.getPath(), amt);
            if ("fluid".equals(type))
                return new InputFluidStack(new FluidStack(BuiltInRegistries.FLUID.get(id), amt));
            return new InputItemStack(new ItemStack(BuiltInRegistries.ITEM.get(id), amt));
        }));


        return builder.group(
                Codec.STRING.fieldOf("operation").forGetter(SmelteryRecipe::getRecipeType),
                Codec.list(singleInputCodec).fieldOf("inputs").forGetter(SmelteryRecipe::getInput),
                Codec.list(singleInputCodec).fieldOf("outputs").forGetter(SmelteryRecipe::getInput)
        ).apply(builder, (operation, inputs, outputs) -> {
            if (!IUCore.register1) {
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

                return new SmelteryRecipe("", Collections.emptyList(), ItemStack.EMPTY);
            } else {
                return new SmelteryRecipe("", Collections.emptyList(), ItemStack.EMPTY);
            }
        });
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, SmelteryRecipe> STREAM_CODEC = StreamCodec.of(SmelterSerializer::toNetwork, SmelterSerializer::fromNetwork);
    public static LinkedList<Integer> integers = new LinkedList<>();

    private static SmelteryRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new SmelteryRecipe("", new ArrayList<>(), ItemStack.EMPTY);
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, SmelteryRecipe p_320586_) {

    }

    @Override
    public MapCodec<SmelteryRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, SmelteryRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
