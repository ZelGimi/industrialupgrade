package com.denfop.api.crafting;

import com.denfop.api.Recipes;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.IngredientInput;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseRecipe implements IShapedRecipe {

    private final ItemStack output;
    private final int[][] inputIndex;

    private final int[] inputIndexCraftingTable = new int[4];
    private final IInputItemStack[][] input;
    private final int size;
    private final NonNullList<Ingredient> listIngridient;
    private final int minX;
    private final int minY;
    private final int x;
    private final int y;
    private final int index;
    private ResourceLocation name;

    public BaseRecipe(ItemStack output, RecipeGrid recipeGrid, List<PartRecipe> partRecipe) {
        this.output = output;
        this.size = recipeGrid.getGrids().size();
        this.minX = recipeGrid.isHasTwoX() ? 2 : 3;
        this.minY = recipeGrid.isHasTwoY() ? 2 : 3;
        this.x = recipeGrid.getX2();
        this.y = recipeGrid.getY2();
        this.index = recipeGrid.getIndex();
        this.inputIndex = new int[recipeGrid.getGrids().size()][9];
        this.input = new IInputItemStack[recipeGrid.getGrids().size()][9];
        for (int j = 0; j < recipeGrid.getGrids().size(); j++) {
            for (PartRecipe recipe : partRecipe) {
                List<Integer> integerList = recipeGrid.getIndexesInGrid(j, recipe);
                for (int i : integerList) {
                    this.inputIndex[j][i] = 1;
                    this.input[j][i] = recipe.getInput();
                }
            }
        }
        this.listIngridient = NonNullList.create();


        for (int x = 0; x < 9; ++x) {
            if (this.inputIndex[0][x] != 0) {
                listIngridient.add(new IngredientInput(this.input[0][x]));
            } else {
                listIngridient.add(Ingredient.EMPTY);
            }
        }
        if (minX == 2 && minY == 2) {
            int j = 0;
            for (int x = 0; x < 3; x++) {
                if (x == this.x) {
                    continue;
                }
                for (int y = 0; y < 3; y++) {
                    if (this.y == y) {
                        continue;
                    }
                    this.inputIndexCraftingTable[j] = x + y * 3;
                    j++;
                }
            }
        }
        Recipes.registerRecipe(this);
    }

    public int[] getInputIndex() {
        return inputIndex[0];
    }

    public IInputItemStack[] getInput() {
        return input[0];
    }

    public ItemStack getOutput() {
        return output;
    }


    public NonNullList<Ingredient> getIngredients() {
        return listIngridient;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public int getRecipeWidth() {
        return 3;
    }

    @Override
    public int getRecipeHeight() {
        return 3;
    }

    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        return this.matches(inv) != ItemStack.EMPTY;
    }

    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.output.copy();
    }


    public ItemStack matches(final InventoryCrafting inv) {
        if (inv.getWidth() < 3 || inv.getHeight() < 3) {
            if (this.minY == 2 && this.minX == 2 && inv.getWidth() == 2 && inv.getHeight() == 2) {

                for (int i = 0; i < 4; i++) {
                    ItemStack offer = inv.getStackInSlot(i);
                    if (this.inputIndex[this.index][this.inputIndexCraftingTable[i]] == 0 && !offer.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (this.inputIndex[this.index][this.inputIndexCraftingTable[i]] != 0 && offer.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (this.inputIndex[this.index][this.inputIndexCraftingTable[i]] == 0 && offer.isEmpty()) {
                        continue;
                    }
                    if (!this.input[this.index][this.inputIndexCraftingTable[i]].matches(offer)) {
                        return ItemStack.EMPTY;
                    }
                }
                return this.output.copy();
            } else {
                return ItemStack.EMPTY;
            }
        }
        for (int j = 0; j < this.size; j++) {
            if (j != this.size - 1) {
                boolean has = true;
                for (int i = 0; i < inv.getHeight() * inv.getWidth(); i++) {
                    ItemStack offer = inv.getStackInSlot(i);
                    if (this.inputIndex[j][i] == 0 && !offer.isEmpty()) {
                        has = false;
                        break;
                    }
                    if (this.inputIndex[j][i] != 0 && offer.isEmpty()) {
                        has = false;
                        break;
                    }
                    if (this.inputIndex[j][i] == 0 && offer.isEmpty()) {
                        continue;
                    }
                    if (!this.input[j][i].matches(offer)) {
                        has = false;
                        break;
                    }
                }
                if (has) {
                    return this.output.copy();
                }
            } else {
                for (int i = 0; i < 9; i++) {
                    ItemStack offer = inv.getStackInSlot(i);
                    if (this.inputIndex[j][i] == 0 && !offer.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (this.inputIndex[j][i] != 0 && offer.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (this.inputIndex[j][i] == 0 && offer.isEmpty()) {
                        continue;
                    }
                    if (!this.input[j][i].matches(offer)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return this.output.copy();
    }

    @Override
    public boolean canFit(final int width, final int height) {
        return 3 == width && height == 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;

    }

    @Override
    public IRecipe setRegistryName(final ResourceLocation name) {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return name;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }

}
