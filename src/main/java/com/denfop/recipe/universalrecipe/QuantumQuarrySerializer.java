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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

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

            return new QuantumQuarryRecipe(recipeType, new ArrayList<>(), recipeOperation);
        });
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, QuantumQuarryRecipe> STREAM_CODEC = StreamCodec.of(QuantumQuarrySerializer::toNetwork, QuantumQuarrySerializer::fromNetwork);

    private static QuantumQuarryRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new QuantumQuarryRecipe("", new ArrayList<>(), "");
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, QuantumQuarryRecipe p_320586_) {

    }

    @Override
    public MapCodec<QuantumQuarryRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, QuantumQuarryRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
