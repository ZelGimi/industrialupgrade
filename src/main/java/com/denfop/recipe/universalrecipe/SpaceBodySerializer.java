package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.BaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.rovers.enums.EnumTypeRovers;
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

import static com.denfop.api.space.SpaceInit.regBaseResource;

public class SpaceBodySerializer implements RecipeSerializer<SpaceBodyRecipe> {

    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceBodyRecipe> STREAM_CODEC = StreamCodec.of(SpaceBodySerializer::toNetwork, SpaceBodySerializer::fromNetwork);
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
    public static final MapCodec<SpaceBodyRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("body").forGetter(SpaceBodyRecipe::bodyName),
            Codec.INT.fieldOf("percent").forGetter(SpaceBodyRecipe::percent),
            Codec.INT.fieldOf("chance").forGetter(SpaceBodyRecipe::chance),
            Codec.STRING.fieldOf("typeRover").forGetter(SpaceBodyRecipe::typeRover),
            Codec.STRING.fieldOf("typeOperation").forGetter(SpaceBodyRecipe::typeOperation),
            Codec.list(singleInputCodec).fieldOf("inputs").forGetter(SpaceBodyRecipe::getInputs)
    ).apply(instance, (bodyName, percent, chance, roverType, operationType, input) -> {
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

        return new SpaceBodyRecipe("", Collections.emptyList(), "");
    }));

    private static SpaceBodyRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new SpaceBodyRecipe("", new ArrayList<>(), "");
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, SpaceBodyRecipe p_320586_) {

    }

    @Override
    public MapCodec<SpaceBodyRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, SpaceBodyRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
