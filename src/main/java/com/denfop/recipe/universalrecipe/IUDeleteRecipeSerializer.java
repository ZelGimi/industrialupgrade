package com.denfop.recipe.universalrecipe;

import com.denfop.api.Recipes;
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
import java.util.List;

public class IUDeleteRecipeSerializer implements RecipeSerializer<IURecipeDelete> {
    public static final MapCodec<IURecipeDelete> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> {

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


        return builder.group(
                Codec.STRING.fieldOf("recipe_type").forGetter(IURecipeDelete::getRecipeType),
                Codec.BOOL.fieldOf("isFluidRecipe").orElse(false).forGetter(IURecipeDelete::isFluid),
                Codec.list(singleInputCodec).fieldOf("output").forGetter(IURecipeDelete::getInputsAll),
                Codec.BOOL.fieldOf("isRemoveAll").orElse(false).forGetter(IURecipeDelete::isRemoveAll)
        ).apply(builder, (recipeType, isFluidRecipe, inputs1, removeAll) -> {

            List<ItemStack> outputs1 = new ArrayList<>();
            List<FluidStack> outputsFluid = new ArrayList<>();
            for (IInputItemStack o : inputs1) {
                if (o instanceof InputFluidStack) outputsFluid.add(((InputFluidStack) o).getFluid());
                else outputs1.add(o.getInputs().get(0));
            }
            if (isFluidRecipe && !outputsFluid.isEmpty()) {
                Recipes.recipes.addFluidRemoveRecipe(recipeType, outputsFluid.get(0), removeAll);

            }
            if (!outputs1.isEmpty()) {
                Recipes.recipes.addRemoveRecipe(recipeType, outputs1.get(0), removeAll);
            }
            return new IURecipeDelete(recipeType, isFluidRecipe, inputs1, removeAll);
        });
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, IURecipeDelete> STREAM_CODEC = StreamCodec.of(IUDeleteRecipeSerializer::toNetwork, IUDeleteRecipeSerializer::fromNetwork);

    private static IURecipeDelete fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new IURecipeDelete("", false, new ArrayList<>(), false);
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, IURecipeDelete p_320586_) {

    }

    @Override
    public MapCodec<IURecipeDelete> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, IURecipeDelete> streamCodec() {
        return STREAM_CODEC;
    }

}
