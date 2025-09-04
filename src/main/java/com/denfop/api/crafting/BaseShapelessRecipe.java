package com.denfop.api.crafting;

import com.denfop.api.Recipes;
import com.denfop.items.ItemToolCrafting;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.IngredientInput;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipe.InputOreDict;
import com.denfop.register.Register;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseShapelessRecipe extends ShapelessRecipe {

    final NonNullList<Ingredient> listIngridient;
    private final ItemStack output;
    private final List<IInputItemStack> recipeInputList;
    private final String id;
    private ResourceLocation name;

    public BaseShapelessRecipe(ResourceLocation id, String group, ItemStack output, List<IInputItemStack> recipeInputList) {
        super(id, group, output, NonNullList.create());
        this.id = "";

        this.output = output;
        this.recipeInputList = recipeInputList;
        this.listIngridient = NonNullList.create();


        for (IInputItemStack input : this.recipeInputList) {
            this.listIngridient.add(new IngredientInput(input).getInput());

        }

    }

    public BaseShapelessRecipe(ItemStack output, List<IInputItemStack> recipeInputList) {
        super(ResourceLocation.tryParse("minecraft:minecraft"), "", output, NonNullList.create());
        this.output = output;
        this.recipeInputList = recipeInputList;
        listIngridient = NonNullList.create();


        for (IInputItemStack input : this.recipeInputList) {
            listIngridient.add(new IngredientInput(input).getInput());

        }

        this.id = Recipes.registerRecipe(this);
    }

    public static BaseShapelessRecipe create(ResourceLocation id, CustomPacketBuffer customPacketBuffer) {
        try {
            String group = (String) DecoderHandler.decode(customPacketBuffer);
            ItemStack output = (ItemStack) DecoderHandler.decode(customPacketBuffer);


            List<IInputItemStack> recipeInputList = new ArrayList<>();
            int size = customPacketBuffer.readInt();
            for (int i = 0; i < size; i++) {
                recipeInputList.add(InputItemStack.create((CompoundTag) DecoderHandler.decode(customPacketBuffer)));
            }
            return new BaseShapelessRecipe(id, group, output, recipeInputList);
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

    public ItemStack matches(final CraftingContainer inv) {

        for (int i = 0; i < recipeInputList.size(); i++) {
            IInputItemStack input = this.recipeInputList.get(i);
            if (input instanceof InputOreDict && input.hasTag() && input.getInputs().isEmpty())
                recipeInputList.set(i, new InputOreDict(input.getTag(), input.getAmount()));
        }
        List<IInputItemStack> recipeInputList1 = new ArrayList<>(recipeInputList);
        for (int i = 0; i < inv.getContainerSize(); i++) {
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
            for (int i = 0; i < inv.getContainerSize(); i++) {
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
    public boolean matches(CraftingContainer p_44002_, Level p_44003_) {
        return matches(p_44002_) != ItemStack.EMPTY;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= this.recipeInputList.size();
    }

    @Override
    public ItemStack getResultItem() {
        return this.output.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer p_44004_) {
        final NonNullList<ItemStack> list = NonNullList.withSize(p_44004_.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < p_44004_.getContainerSize(); i++) {
            ItemStack stack = p_44004_.getItem(i);
            if (stack.getItem() instanceof ItemToolCrafting) {
                stack = stack.getItem().getCraftingRemainingItem(stack);
                list.set(i, stack);
            }
        }
        return list;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {

        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (IInputItemStack input : recipeInputList) {
            ingredients.add(input.hasTag() ? Ingredient.of(input.getTag()) : Ingredient.of(input.getInputs().get(0)));

        }
        return ingredients;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_SHAPELESS_RECIPE.get();
    }

    public void toNetwork(FriendlyByteBuf buf) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
        try {
            EncoderHandler.encode(packetBuffer, getGroup());
            EncoderHandler.encode(packetBuffer, output);

            packetBuffer.writeInt(recipeInputList.size());
            for (IInputItemStack part : recipeInputList)
                EncoderHandler.encode(packetBuffer, part.writeNBT());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
