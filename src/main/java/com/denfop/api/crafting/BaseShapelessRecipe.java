package com.denfop.api.crafting;

import com.denfop.api.Recipes;
import com.denfop.items.ItemToolCrafting;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipe.InputOreDict;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BaseShapelessRecipe implements CraftingRecipe {

    final NonNullList<Ingredient> listIngridient;
    private final ItemStack output;
    private final List<IInputItemStack> recipeInputList;
    private final String id;
    private ResourceLocation name;

    public BaseShapelessRecipe(ItemStack output, List<IInputItemStack> recipeInputList) {
        this.output = output;
        this.recipeInputList = recipeInputList;
        listIngridient = NonNullList.create();

        for (IInputItemStack recipeInput : this.recipeInputList) {
            if (recipeInput.getInputs().size() == 1 && !recipeInput.getInputs().get(0).getComponents().isEmpty()) {
                List<TypedDataComponent<?>> list = recipeInput.getInputs().get(0).getComponents().stream().collect(Collectors.toList());
                list.removeIf(typedDataComponent -> ModUtils.ignoredNbtKeys.contains(typedDataComponent.type()));
                if (!list.isEmpty()) {
                    HolderSet<Item> holders = HolderSet.direct(recipeInput.getInputs().get(0).getItemHolder());
                    listIngridient.add(new Ingredient(new DataComponentIngredient(holders, DataComponentPredicate.allOf(recipeInput.getInputs().get(0).getComponents()), false)));
                } else {
                    if (recipeInput instanceof InputOreDict)
                        listIngridient.add(Ingredient.of(recipeInput.getTag()));
                    else
                        listIngridient.add(Ingredient.of(recipeInput.getInputs().toArray(new ItemStack[0])));
                }
            } else {
                if (recipeInput instanceof InputOreDict)
                    listIngridient.add(Ingredient.of(recipeInput.getTag()));
                else
                    listIngridient.add(Ingredient.of(recipeInput.getInputs().toArray(new ItemStack[0])));
            }

        }

        this.id = Recipes.registerRecipe(this);
    }

    public static BaseShapelessRecipe create(CustomPacketBuffer customPacketBuffer) {
        try {
            ItemStack output = (ItemStack) DecoderHandler.decode(customPacketBuffer);
            List<IInputItemStack> partRecipes = new ArrayList<>();
            int size = (int) customPacketBuffer.readInt();
            for (int i = 0; i < size; i++) {
                partRecipes.add(InputItemStack.create((CompoundTag) DecoderHandler.decode(customPacketBuffer), customPacketBuffer.registryAccess()));
            }
            return new BaseShapelessRecipe(output, partRecipes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<IInputItemStack> getRecipeInputList() {
        return recipeInputList;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack matches(final CraftingInput inv) {

        for (int i = 0; i < recipeInputList.size(); i++) {
            IInputItemStack input = this.recipeInputList.get(i);
            if (input instanceof InputOreDict && input.hasTag() && input.getInputs().isEmpty())
                recipeInputList.set(i, new InputOreDict(input.getTag(), input.getAmount()));
        }
        List<IInputItemStack> recipeInputList1 = new ArrayList<>(recipeInputList);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            final Iterator<IInputItemStack> iter = recipeInputList1.iterator();
            while (iter.hasNext()) {
                IInputItemStack recipeInput = iter.next();
                if (recipeInput.matches(stack)) {
                    iter.remove();
                    break;
                }
            }
        }
        if (!recipeInputList1.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            int col = 0;
            for (int i = 0; i < inv.size(); i++) {
                ItemStack stack = inv.getItem(i);
                if (!stack.isEmpty()) {
                    col++;
                }
            }
            if (col == recipeInputList.size()) {
                return this.output.copy();
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public boolean matches(CraftingInput p_346065_, Level p_345375_) {
        return matches(p_346065_) != ItemStack.EMPTY;
    }

    @Override
    public ItemStack assemble(CraftingInput p_345149_, HolderLookup.Provider p_346030_) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= this.recipeInputList.size();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider p_336125_) {
        return this.output.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput p_345383_) {
        final NonNullList<ItemStack> list = NonNullList.withSize(p_345383_.size(), ItemStack.EMPTY);
        for (int i = 0; i < p_345383_.size(); i++) {
            ItemStack stack = p_345383_.getItem(i);
            if (stack.getItem() instanceof ItemToolCrafting) {
                stack = stack.getItem().getCraftingRemainingItem(stack);
                list.set(i, stack);
            }
        }
        return list;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {

        return listIngridient;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_SHAPELESS.get();
    }

    public void toNetwork(RegistryFriendlyByteBuf buf) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
        try {

            EncoderHandler.encode(packetBuffer, output);
            packetBuffer.writeInt(recipeInputList.size());
            for (IInputItemStack part : recipeInputList)
                EncoderHandler.encode(packetBuffer, part.writeNBT(buf.registryAccess()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
