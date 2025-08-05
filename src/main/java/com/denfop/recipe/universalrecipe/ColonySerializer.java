package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
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

import static com.denfop.api.space.SpaceInit.regColonyBaseResource;

public class ColonySerializer implements RecipeSerializer<ColonyRecipe> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ColonyRecipe> STREAM_CODEC = StreamCodec.of(ColonySerializer::toNetwork, ColonySerializer::fromNetwork);
    static Codec<IInputItemStack> singleInputCodec = RecordCodecBuilder.create(inst -> inst.group(
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
    public static final MapCodec<ColonyRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("body").forGetter(c -> c.bodyName),
            Codec.SHORT.fieldOf("level").forGetter(c -> c.level),
            Codec.list(singleInputCodec).fieldOf("inputs").forGetter(ColonyRecipe::getInputs)
    ).apply(instance, (bodyName, level, inputs) -> {
        IBody body = SpaceNet.instance.getBodyFromName(bodyName.toLowerCase());

        for (IInputItemStack itemStack : inputs) {
            if (itemStack instanceof InputItemStack)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addItemStack(body, (short) level, ((InputItemStack) itemStack).input));
            if (itemStack instanceof InputFluidStack)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addFluidStack(body, (short) level, ((InputFluidStack) itemStack).getFluid()));
            if (itemStack instanceof InputOreDict)
                regColonyBaseResource.add(() -> SpaceNet.instance.getColonieNet().addItemStack(body, (short) level, itemStack.getInputs().get(0)));

        }
        return new ColonyRecipe("", Collections.emptyList(), "");
    }));

    private static ColonyRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new ColonyRecipe("", new ArrayList<>(), "");
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, ColonyRecipe p_320586_) {

    }

    @Override
    public MapCodec<ColonyRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, ColonyRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
