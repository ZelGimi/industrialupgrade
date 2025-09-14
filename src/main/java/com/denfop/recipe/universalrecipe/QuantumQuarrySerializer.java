package com.denfop.recipe.universalrecipe;

import com.denfop.IUCore;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputFluidStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipe.InputOreDict;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class QuantumQuarrySerializer implements RecipeSerializer<QuantumQuarryRecipe> {
    public static final MapCodec<QuantumQuarryRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> {

        Codec<IInputItemStack> singleInputCodec = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("type").forGetter(i -> i instanceof InputFluidStack ? "fluid" : "item"),
                ResourceLocation.CODEC.fieldOf("id").forGetter(i -> BuiltInRegistries.ITEM.getKey(i.getInputs().get(0).getItem())),
                Codec.INT.fieldOf("amount").orElse(1).forGetter(i -> i.getInputs().get(0).getCount())
        ).apply(inst, (type, id, amt) -> {
            if ("tag".equals(type))
                return new InputOreDict(id.getNamespace() + ":" + id.getPath(), amt);
            else return new InputItemStack(new ItemStack(BuiltInRegistries.ITEM.get(id), amt));
        }));


        return builder.group(
                Codec.STRING.fieldOf("recipe_type").forGetter(QuantumQuarryRecipe::getRecipeType),
                Codec.STRING.fieldOf("recipeOperation").forGetter(QuantumQuarryRecipe::getTypeOperation),
                Codec.list(singleInputCodec).fieldOf("inputs").forGetter(QuantumQuarryRecipe::getInputs)
        ).apply(builder, (recipeType, recipeOperation, inputs) -> {

            List<ItemStack> input = new ArrayList<>();
            for (IInputItemStack o : inputs) {
                input.add(o.getInputs().get(0));
            }

            switch (recipeOperation) {
                case "default":
                    switch (recipeOperation) {
                        case "add":
                            IUCore.list_adding.addAll(input);
                            break;
                        case "remove":
                            IUCore.list_removing.addAll(input);
                            break;
                    }
                    break;
                case "furnace":
                    switch (recipeOperation) {
                        case "add":
                            IUCore.list_furnace_adding.addAll(input);
                            break;
                        case "remove":
                            IUCore.list_furnace_removing.addAll(input);
                            break;
                    }
                    break;
                case "macerator":
                    switch (recipeOperation) {
                        case "add":
                            IUCore.list_crushed_adding.addAll(input);
                            break;
                        case "remove":
                            IUCore.list_crushed_removing.addAll(input);
                            break;
                    }
                    break;
                case "comb_macerator":
                    switch (recipeOperation) {
                        case "add":
                            IUCore.list_comb_crushed_adding.addAll(input);
                            break;
                        case "remove":
                            IUCore.list_comb_crushed_removing.addAll(input);
                            break;
                    }
                    break;
            }

            return new QuantumQuarryRecipe(recipeType, recipeOperation, inputs);
        });
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, QuantumQuarryRecipe> STREAM_CODEC =
            StreamCodec.of(

                    (buf, recipe) -> {
                        buf.writeUtf(recipe.getRecipeType());
                        buf.writeUtf(recipe.getTypeOperation());

                        buf.writeVarInt(recipe.getInputs().size());
                        for (IInputItemStack stack : recipe.getInputs()) {
                            if (stack instanceof InputItemStack itemStack) {
                                buf.writeUtf("item");
                                buf.writeResourceLocation(BuiltInRegistries.ITEM.getKey(itemStack.input.getItem()));
                                buf.writeVarInt(itemStack.input.getCount());
                            } else if (stack instanceof InputOreDict oreDict) {
                                buf.writeUtf("tag");
                                buf.writeUtf(oreDict.getTag().location().toString());
                                buf.writeVarInt(oreDict.getInputs().get(0).getCount());
                            } else if (stack instanceof InputFluidStack fluidStack) {
                                buf.writeUtf("fluid");
                                buf.writeResourceLocation(BuiltInRegistries.FLUID.getKey(fluidStack.getFluid().getFluid()));
                                buf.writeVarInt(fluidStack.getFluid().getAmount());
                            } else {
                                throw new IllegalArgumentException("Unknown input type: " + stack);
                            }
                        }
                    },

                    buf -> {
                        String recipeType = buf.readUtf();
                        String recipeOperation = buf.readUtf();

                        int size = buf.readVarInt();
                        List<IInputItemStack> inputs = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            String type = buf.readUtf();
                            switch (type) {
                                case "item" -> {
                                    ResourceLocation itemId = buf.readResourceLocation();
                                    int amount = buf.readVarInt();
                                    ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(itemId), amount);
                                    inputs.add(new InputItemStack(itemStack));
                                }
                                case "tag" -> {
                                    String tagId = buf.readUtf();
                                    int amount = buf.readVarInt();
                                    inputs.add(new InputOreDict(tagId, amount));
                                }
                                case "fluid" -> {
                                    ResourceLocation fluidId = buf.readResourceLocation();
                                    int amount = buf.readVarInt();
                                    FluidStack fluidStack = new FluidStack(BuiltInRegistries.FLUID.get(fluidId), amount);
                                    inputs.add(new InputFluidStack(fluidStack));
                                }
                                default -> throw new IllegalArgumentException("Unknown input type: " + type);
                            }
                        }
                        if (!IUCore.updateRecipe){
                            List<ItemStack> input = new ArrayList<>();
                            for (IInputItemStack o : inputs) {
                                input.add(o.getInputs().get(0));
                            }
                            switch (recipeOperation) {
                                case "default":
                                    switch (recipeOperation) {
                                        case "add":
                                            IUCore.list_adding.addAll(input);
                                            break;
                                        case "remove":
                                            IUCore.list_removing.addAll(input);
                                            break;
                                    }
                                    break;
                                case "furnace":
                                    switch (recipeOperation) {
                                        case "add":
                                            IUCore.list_furnace_adding.addAll(input);
                                            break;
                                        case "remove":
                                            IUCore.list_furnace_removing.addAll(input);
                                            break;
                                    }
                                    break;
                                case "macerator":
                                    switch (recipeOperation) {
                                        case "add":
                                            IUCore.list_crushed_adding.addAll(input);
                                            break;
                                        case "remove":
                                            IUCore.list_crushed_removing.addAll(input);
                                            break;
                                    }
                                    break;
                                case "comb_macerator":
                                    switch (recipeOperation) {
                                        case "add":
                                            IUCore.list_comb_crushed_adding.addAll(input);
                                            break;
                                        case "remove":
                                            IUCore.list_comb_crushed_removing.addAll(input);
                                            break;
                                    }
                                    break;
                            }
                        }
                        return new QuantumQuarryRecipe(recipeType, recipeOperation, inputs);
                    }
            );


    @Override
    public MapCodec<QuantumQuarryRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, QuantumQuarryRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
