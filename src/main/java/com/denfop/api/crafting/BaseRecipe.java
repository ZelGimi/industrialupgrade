package com.denfop.api.crafting;

import com.denfop.api.Recipes;
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
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseRecipe extends ShapedRecipe {

    private final ItemStack output;
    private final int[][] inputIndex;

    private final int[] inputIndexCraftingTable = new int[4];
    private final IInputItemStack[][] input;
    private final int size;
    private final NonNullList<Ingredient> listIngridient;
    private final NonNullList<IInputItemStack> listInput;
    private final int minX;
    private final int minY;
    private final int x;
    private final int y;
    private final int index;
    private final String id;
    private final List<PartRecipe> partRecipe;
    private final RecipeGrid recipeGrid;

    public BaseRecipe(ResourceLocation id, String group, ItemStack output, RecipeGrid recipeGrid, List<PartRecipe> partRecipe) {
        super(id, group, recipeGrid.isHasTwoX() ? 2 : 3, recipeGrid.isHasTwoY() ? 2 : 3, NonNullList.create(), output);
        this.output = output;
        this.size = recipeGrid.getGrids().size();
        this.minX = recipeGrid.isHasTwoX() ? 2 : 3;
        this.minY = recipeGrid.isHasTwoY() ? 2 : 3;
        this.x = recipeGrid.getX2();
        this.y = recipeGrid.getY2();
        this.index = recipeGrid.getIndex();
        this.inputIndex = new int[recipeGrid.getGrids().size()][9];
        this.input = new IInputItemStack[recipeGrid.getGrids().size()][9];
        this.partRecipe = partRecipe;
        this.recipeGrid = recipeGrid;
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
        this.listInput = NonNullList.create();

        for (int x = 0; x < 9; ++x) {
            if (this.inputIndex[0][x] != 0) {
                listIngridient.add(new IngredientInput(this.input[0][x]).getInput());
                this.listInput.add(this.input[0][x]);
            } else {
                this.listInput.add(InputItemStack.EMPTY);
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
        this.id = id.toString();
    }

    public BaseRecipe(ItemStack output, RecipeGrid recipeGrid, List<PartRecipe> partRecipe) {
        super(ResourceLocation.tryParse("minecraft:minecraft"), "", recipeGrid.isHasTwoX() ? 2 : 3, recipeGrid.isHasTwoY() ? 2 : 3, NonNullList.create(), output);
        this.output = output;
        this.size = recipeGrid.getGrids().size();
        this.minX = recipeGrid.isHasTwoX() ? 2 : 3;
        this.minY = recipeGrid.isHasTwoY() ? 2 : 3;
        this.x = recipeGrid.getX2();
        this.y = recipeGrid.getY2();
        this.index = recipeGrid.getIndex();
        this.inputIndex = new int[recipeGrid.getGrids().size()][9];
        this.input = new IInputItemStack[recipeGrid.getGrids().size()][9];
        this.partRecipe = partRecipe;
        this.recipeGrid = recipeGrid;
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
        this.listInput = NonNullList.create();

        for (int x = 0; x < 9; ++x) {
            if (this.inputIndex[0][x] != 0) {
                listIngridient.add(new IngredientInput(this.input[0][x]).getInput());
                this.listInput.add(this.input[0][x]);
            } else {
                this.listInput.add(InputItemStack.EMPTY);
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
        this.id = Recipes.registerRecipe(this);
    }

    public static BaseRecipe create(ResourceLocation id, CustomPacketBuffer customPacketBuffer) {
        try {
            String group = (String) DecoderHandler.decode(customPacketBuffer);
            ItemStack output = (ItemStack) DecoderHandler.decode(customPacketBuffer);
            List<String> args = (List<String>) DecoderHandler.decode(customPacketBuffer);
            RecipeGrid grid = new RecipeGrid(args);
            List<PartRecipe> partRecipes = new ArrayList<>();
            int size = (int) customPacketBuffer.readInt();
            for (int i = 0; i < size; i++) {
                partRecipes.add(new PartRecipe((String) DecoderHandler.decode(customPacketBuffer), InputItemStack.create((CompoundTag) DecoderHandler.decode(customPacketBuffer))));
            }
            return new BaseRecipe(id, group, output, grid, partRecipes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NonNullList<IInputItemStack> getListInput() {
        return listInput;
    }

    public List<PartRecipe> getPartRecipe() {
        return partRecipe;
    }

    public RecipeGrid getRecipeGrid() {
        return recipeGrid;
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

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        return this.matches(pInv) != ItemStack.EMPTY;
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return 3 == p_43999_ && p_44000_ == 3;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer p_44004_) {
        return NonNullList.withSize(p_44004_.getContainerSize(), ItemStack.EMPTY);
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (int x = 0; x < 9; ++x) {
            if (this.inputIndex[0][x] != 0) {

                ingredients.add(input[0][x].hasTag() ? Ingredient.of(input[0][x].getTag()) : Ingredient.of(input[0][x].getInputs().get(0)));
            } else {
                ingredients.add(Ingredient.EMPTY);
            }
        }
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_SHAPED_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public int getRecipeWidth() {
        return 3;
    }

    @Override
    public int getRecipeHeight() {
        return 3;
    }

    public ItemStack matches(final CraftingContainer inv) {
        int width = (int) Math.sqrt(inv.getContainerSize());
        int height = inv.getContainerSize() / width;

        if (width < 3 || height < 3) {
            if (this.minY == 2 && this.minX == 2 && width == 2 && height == 2) {
                for (int i = 0; i < 4; i++) {
                    ItemStack offer = inv.getItem(i);
                    if (this.inputIndex[this.index][this.inputIndexCraftingTable[i]] == 0 && !offer.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (this.inputIndex[this.index][this.inputIndexCraftingTable[i]] != 0 && offer.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (this.inputIndex[this.index][this.inputIndexCraftingTable[i]] == 0 && offer.isEmpty()) {
                        continue;
                    }
                    if (this.input[this.index][this.inputIndexCraftingTable[i]] instanceof InputOreDict && this.input[this.index][this.inputIndexCraftingTable[i]].hasTag() && this.input[this.index][this.inputIndexCraftingTable[i]].getInputs().isEmpty())
                        this.input[this.index][this.inputIndexCraftingTable[i]] = new InputOreDict(this.input[this.index][this.inputIndexCraftingTable[i]].getTag(), this.input[this.index][this.inputIndexCraftingTable[i]].getAmount());

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
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    ItemStack offer = inv.getItem(i);
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
                    if (input[j][i] instanceof InputOreDict && input[j][i].hasTag() && input[j][i].getInputs().isEmpty())
                        input[j][i] = new InputOreDict(this.input[j][i].getTag(), this.input[j][i].getAmount());
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
                    ItemStack offer = inv.getItem(i);
                    if (this.inputIndex[j][i] == 0 && !offer.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (this.inputIndex[j][i] != 0 && offer.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (this.inputIndex[j][i] == 0 && offer.isEmpty()) {
                        continue;
                    }
                    if (input[j][i] instanceof InputOreDict && input[j][i].hasTag() && input[j][i].getInputs().isEmpty())
                        input[j][i] = new InputOreDict(this.input[j][i].getTag(), this.input[j][i].getAmount());
                    if (!this.input[j][i].matches(offer)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return this.output.copy();
    }

    public void toNetwork(FriendlyByteBuf buf) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
        try {
            EncoderHandler.encode(packetBuffer, getGroup());
            EncoderHandler.encode(packetBuffer, output);
            recipeGrid.encode(packetBuffer);
            packetBuffer.writeInt(partRecipe.size());
            for (PartRecipe part : partRecipe)
                part.encode(packetBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
