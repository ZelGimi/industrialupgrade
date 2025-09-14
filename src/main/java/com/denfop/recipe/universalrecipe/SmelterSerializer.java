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
                Codec.STRING.fieldOf("operation").forGetter(SmelteryRecipe::getOperation),
                Codec.list(singleInputCodec).fieldOf("inputs").forGetter(SmelteryRecipe::getInputs),
                Codec.list(singleInputCodec).fieldOf("outputs").forGetter(SmelteryRecipe::getOutputs)
        ).apply(builder, (operation, inputs, outputs) -> {

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

            return new SmelteryRecipe(operation, inputs, outputs);

        });
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, SmelteryRecipe> STREAM_CODEC = StreamCodec.of(

            (buf, recipe) -> {
                buf.writeUtf(recipe.getOperation());
                buf.writeVarInt(recipe.getInputs().size());
                for (IInputItemStack input : recipe.getInputs()) {
                    if (input instanceof InputFluidStack) {
                        buf.writeUtf("fluid");
                        buf.writeResourceLocation(BuiltInRegistries.FLUID.getKey(((InputFluidStack) input).getFluid().getFluid()));
                        buf.writeVarInt(((InputFluidStack) input).getFluid().getAmount());
                    } else if (input instanceof InputOreDict) {
                        buf.writeUtf("tag");
                        buf.writeUtf(input.getTag().location().toString());
                        buf.writeVarInt(input.getAmount());
                    } else if (input instanceof InputItemStack) {
                        buf.writeUtf("item");
                        buf.writeResourceLocation(BuiltInRegistries.ITEM.getKey(((InputItemStack) input).input.getItem()));
                        buf.writeVarInt(input.getAmount());
                    }
                }


                buf.writeVarInt(recipe.getOutputs().size());
                for (IInputItemStack output : recipe.getOutputs()) {
                    if (output instanceof InputFluidStack) {
                        buf.writeUtf("fluid");
                        buf.writeResourceLocation(BuiltInRegistries.FLUID.getKey(((InputFluidStack) output).getFluid().getFluid()));
                        buf.writeVarInt(((InputFluidStack) output).getFluid().getAmount());
                    } else if (output instanceof InputOreDict) {
                        buf.writeUtf("tag");
                        buf.writeUtf(output.getTag().location().toString());
                        buf.writeVarInt(output.getAmount());
                    } else if (output instanceof InputItemStack) {
                        buf.writeUtf("item");
                        buf.writeResourceLocation(BuiltInRegistries.ITEM.getKey(((InputItemStack) output).input.getItem()));
                        buf.writeVarInt(output.getAmount());
                    }
                }
            },


            buf -> {
                String operation = buf.readUtf();
                int inSize = buf.readVarInt();
                List<IInputItemStack> inputs = new ArrayList<>();
                for (int i = 0; i < inSize; i++) {
                    String type = buf.readUtf();
                    switch (type) {
                        case "fluid":
                            ResourceLocation fluidId = buf.readResourceLocation();
                            int amountF = buf.readVarInt();
                            inputs.add(new InputFluidStack(new FluidStack(BuiltInRegistries.FLUID.get(fluidId), amountF)));
                            break;
                        case "tag":
                            String tag = buf.readUtf();
                            int amountT = buf.readVarInt();
                            inputs.add(new InputOreDict(tag, amountT));
                            break;
                        case "item":
                            ResourceLocation itemId = buf.readResourceLocation();
                            int amountI = buf.readVarInt();
                            inputs.add(new InputItemStack(new ItemStack(BuiltInRegistries.ITEM.get(itemId), amountI)));
                            break;
                    }
                }
                int outSize = buf.readVarInt();
                List<IInputItemStack> outputs = new ArrayList<>();
                for (int i = 0; i < outSize; i++) {
                    String type = buf.readUtf();
                    switch (type) {
                        case "fluid":
                            ResourceLocation fluidId = buf.readResourceLocation();
                            int amountF = buf.readVarInt();
                            outputs.add(new InputFluidStack(new FluidStack(BuiltInRegistries.FLUID.get(fluidId), amountF)));
                            break;
                        case "tag":
                            String tag = buf.readUtf();
                            int amountT = buf.readVarInt();
                            outputs.add(new InputOreDict(tag, amountT));
                            break;
                        case "item":
                            ResourceLocation itemId = buf.readResourceLocation();
                            int amountI = buf.readVarInt();
                            inputs.add(new InputItemStack(new ItemStack(BuiltInRegistries.ITEM.get(itemId), amountI)));
                            break;
                    }
                }
                if (!IUCore.updateRecipe){
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
                }
                return new SmelteryRecipe(operation, inputs, outputs);
            }
    );


    @Override
    public MapCodec<SmelteryRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, SmelteryRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
