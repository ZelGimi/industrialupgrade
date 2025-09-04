package com.denfop.recipe.universalrecipe;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputFluidStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipe.InputOreDict;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;

public class IURecipeSerializer implements RecipeSerializer<IURecipe> {
    public static final MapCodec<IURecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> {

        Codec<IInputItemStack> singleInputCodec = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("type").forGetter(i -> i instanceof InputFluidStack ? "fluid" : "item"),
                ResourceLocation.CODEC.fieldOf("id").forGetter(i -> BuiltInRegistries.ITEM.getKey(i.getInputs().get(0).getItem())),
                Codec.INT.fieldOf("amount").orElse(1).forGetter(i -> i.getInputs().get(0).getCount())
        ).apply(inst, (type, id, amt) -> {
            if ("fluid".equals(type))
                return new InputFluidStack(new FluidStack(BuiltInRegistries.FLUID.get(id), amt));
            else if ("tag".equals(type))
                return new InputOreDict(id.getNamespace() + ":" + id.getPath(), amt);
            else return new InputItemStack(new ItemStack(BuiltInRegistries.ITEM.get(id), amt));
        }));

        Codec<Object> singleOutputCodec = RecordCodecBuilder.<Object>create(inst -> inst.group(
                Codec.STRING.fieldOf("type").forGetter(o -> o instanceof FluidStack ? "fluid" : "item"),
                ResourceLocation.CODEC.fieldOf("id").forGetter(o -> o instanceof FluidStack ? BuiltInRegistries.FLUID.getKey(((FluidStack) o).getFluid()) : BuiltInRegistries.ITEM.getKey(((ItemStack) o).getItem())),
                Codec.INT.fieldOf("amount").orElse(1).forGetter(o -> o instanceof FluidStack ? ((FluidStack) o).getAmount() : ((ItemStack) o).getCount())
        ).apply(inst, (type, id, amt) -> {
            if ("fluid".equals(type))
                return new FluidStack(BuiltInRegistries.FLUID.get(id), amt);
            else return new ItemStack(BuiltInRegistries.ITEM.get(id), amt);
        }));

        return builder.group(
                Codec.STRING.fieldOf("recipe_type").forGetter(IURecipe::getRecipeType),
                Codec.BOOL.optionalFieldOf("isFluidRecipe",false).forGetter(IURecipe::isFluid),
                Codec.list(singleInputCodec).fieldOf("inputs").forGetter(IURecipe::getInputs),
                Codec.list(singleOutputCodec).fieldOf("outputs").forGetter(r -> Collections.singletonList(r.getOutputs())),
                Codec.unboundedMap(Codec.STRING, Codec.either(Codec.STRING, Codec.DOUBLE))
                        .xmap(
                                m -> {
                                    Map<String, Object> res = new java.util.HashMap<>();
                                    m.forEach((k, v) -> res.put(k, v.map(left -> left, right -> right)));
                                    return res;
                                },
                                m -> {
                                    Map<String, com.mojang.datafixers.util.Either<String, Double>> enc = new java.util.HashMap<>();
                                    m.forEach((k, v) -> {
                                        if (v instanceof String)
                                            enc.put(k, com.mojang.datafixers.util.Either.left((String) v));
                                        else if (v instanceof Number)
                                            enc.put(k, com.mojang.datafixers.util.Either.right(((Number) v).doubleValue()));
                                    });
                                    return enc;
                                }
                        ).fieldOf("params").forGetter(IURecipe::getParams)
        ).apply(builder, (recipeType, isFluidRecipe, inputs1, outputs, params) -> {

            List<ItemStack> outputs1 = new ArrayList<>();
            List<FluidStack> outputsFluid = new ArrayList<>();
            for (Object o : outputs) {
                if (o instanceof FluidStack) outputsFluid.add((FluidStack) o);
                else outputs1.add((ItemStack) o);
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
            List<FluidStack> fluidStacks = new ArrayList<>();
            List<IInputItemStack> inputs = new ArrayList<>();
            for (IInputItemStack o : inputs1) {
                if (o instanceof InputFluidStack)
                    fluidStacks.add(((InputFluidStack) o).getFluid());
                else
                    inputs.add(o);
            }
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
            return new IURecipe(recipeType, isFluidRecipe, inputs, fluidStacks, outputs1, outputsFluid, params);
        });
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, IURecipe> STREAM_CODEC = StreamCodec.of(IURecipeSerializer::toNetwork, IURecipeSerializer::fromNetwork);

    private static IURecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new IURecipe("", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>());
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, IURecipe p_320586_) {

    }

    @Override
    public MapCodec<IURecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, IURecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
