package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.BaseResource;
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
import java.util.List;

import static com.denfop.IUCore.register;
import static com.denfop.IUCore.updateRecipe;
import static com.denfop.api.space.SpaceInit.regBaseResource;

public class SpaceBodySerializer implements RecipeSerializer<SpaceBodyRecipe> {

    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceBodyRecipe> STREAM_CODEC =
            StreamCodec.of(
                    (buf, recipe) -> {
                        buf.writeUtf(recipe.bodyName);
                        buf.writeVarInt(recipe.percent);
                        buf.writeVarInt(recipe.chance);
                        buf.writeUtf(recipe.roverType);
                        buf.writeUtf(recipe.operationType);

                        buf.writeVarInt(recipe.input.size());
                        for (IInputItemStack inputStack : recipe.input) {
                            if (inputStack instanceof InputItemStack stack) {
                                buf.writeUtf("item");
                                buf.writeUtf(BuiltInRegistries.ITEM.getKey(stack.input.getItem()).toString());
                                buf.writeVarInt(stack.input.getCount());
                            } else if (inputStack instanceof InputFluidStack fluid) {
                                buf.writeUtf("fluid");
                                buf.writeUtf(BuiltInRegistries.FLUID.getKey(fluid.getFluid().getFluid()).toString());
                                buf.writeVarInt(fluid.getFluid().getAmount());
                            } else if (inputStack instanceof InputOreDict ore) {
                                buf.writeUtf("tag");
                                buf.writeUtf(ore.getTag().location().toString());
                                buf.writeVarInt(ore.getInputs().get(0).getCount());
                            } else {
                                throw new IllegalArgumentException("Unknown input type: " + inputStack);
                            }
                        }
                    },
                    buf -> {
                        String bodyName = buf.readUtf();
                        int percent = buf.readVarInt();
                        int chance = buf.readVarInt();
                        String typeRover = buf.readUtf();
                        String typeOperation = buf.readUtf();

                        int size = buf.readVarInt();
                        List<IInputItemStack> inputs = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            String type = buf.readUtf();
                            String id = buf.readUtf();
                            int amount = buf.readVarInt();

                            switch (type) {
                                case "item" -> inputs.add(new InputItemStack(
                                        new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(id)), amount)
                                ));
                                case "fluid" -> inputs.add(new InputFluidStack(
                                        new FluidStack(BuiltInRegistries.FLUID.get(ResourceLocation.parse(id)), amount)
                                ));
                                case "tag" -> inputs.add(new InputOreDict(id, amount));
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
                        return new SpaceBodyRecipe(bodyName, percent, chance, typeRover, typeOperation, inputs);
                    }
            );
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


        if (operationType.equals("add")) {
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

        return new SpaceBodyRecipe(bodyName, percent, chance, roverType, operationType, input);
    }));


    @Override
    public MapCodec<SpaceBodyRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, SpaceBodyRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
