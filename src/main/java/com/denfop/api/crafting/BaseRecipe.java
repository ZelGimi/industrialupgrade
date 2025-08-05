package com.denfop.api.crafting;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.element.CraftingInputIndustrial;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipe.InputOreDict;
import com.denfop.recipe.ShapedRecipePattern;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseRecipe implements CraftingRecipe {

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
    private final CraftingBookCategory category;
    private final String grop;

    public BaseRecipe(String p_250221_, CraftingBookCategory p_250716_, ItemStack output, RecipeGrid recipeGrid, List<PartRecipe> partRecipe) {
        this.output = output;
        this.category = p_250716_;
        this.grop = p_250221_;
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
                IInputItemStack recipeInput = this.input[0][x];
                if (recipeInput.getInputs().size() == 1 && !recipeInput.getInputs().get(0).getComponents().isEmpty()) {
                    HolderSet<Item> holders = HolderSet.direct(recipeInput.getInputs().get(0).getItemHolder());
                    listIngridient.add(new Ingredient(new DataComponentIngredient(holders, DataComponentPredicate.allOf(recipeInput.getInputs().get(0).getComponents()), false)));
                } else {
                    if (recipeInput instanceof InputOreDict)
                        listIngridient.add(Ingredient.of(recipeInput.getTag()));
                    else
                        listIngridient.add(Ingredient.of(recipeInput.getInputs().toArray(new ItemStack[0])));
                    this.listInput.add(this.input[0][x]);
                }
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
        this.id = "";
    }

    public BaseRecipe(ItemStack output, RecipeGrid recipeGrid, List<PartRecipe> partRecipe) {
        this.grop = "";
        this.category = CraftingBookCategory.MISC;
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
                IInputItemStack recipeInput = this.input[0][x];
                if (recipeInput.getInputs().size() == 1 && !recipeInput.getInputs().get(0).getComponents().isEmpty()) {
                    HolderSet<Item> holders = HolderSet.direct(recipeInput.getInputs().get(0).getItemHolder());
                    listIngridient.add(new Ingredient(new DataComponentIngredient(holders, DataComponentPredicate.allOf(recipeInput.getInputs().get(0).getComponents()), false)));
                } else {
                    if (recipeInput instanceof InputOreDict)
                        listIngridient.add(Ingredient.of(recipeInput.getTag()));
                    else
                        listIngridient.add(Ingredient.of(recipeInput.getInputs().toArray(new ItemStack[0])));
                    this.listInput.add(this.input[0][x]);
                }
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

    public static BaseRecipe create(CustomPacketBuffer customPacketBuffer) {
        try {
            String group = (String) DecoderHandler.decode(customPacketBuffer);
            int category = (int) DecoderHandler.decode(customPacketBuffer);
            ItemStack output = (ItemStack) DecoderHandler.decode(customPacketBuffer);
            List<String> args = (List<String>) DecoderHandler.decode(customPacketBuffer);
            RecipeGrid grid = new RecipeGrid(args);
            List<PartRecipe> partRecipes = new ArrayList<>();
            int size = (int) customPacketBuffer.readInt();
            for (int i = 0; i < size; i++) {
                partRecipes.add(new PartRecipe((String) DecoderHandler.decode(customPacketBuffer), InputItemStack.create((CompoundTag) DecoderHandler.decode(customPacketBuffer), customPacketBuffer.registryAccess())));
            }
            return new BaseRecipe(group, CraftingBookCategory.values()[category], output, grid, partRecipes);
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
    public boolean matches(CraftingInput p_346065_, Level p_345375_) {
        return this.matches(p_346065_) != ItemStack.EMPTY;
    }

    @Override
    public ItemStack assemble(CraftingInput p_345149_, HolderLookup.Provider p_346030_) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return 3 == p_43999_ && p_44000_ == 3;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider p_336125_) {
        return this.output.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput p_345383_) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(p_345383_.size(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack item = p_345383_.getItem(i);
            if (item.hasCraftingRemainingItem()) {
                nonnulllist.set(i, item.getCraftingRemainingItem());
            }
        }

        return nonnulllist;
    }

    public NonNullList<Ingredient> getIngredients() {
        return listIngridient;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_SHAPED.get();
    }

    public int getWidth() {
        return 3;
    }

    public int getHeight() {
        return 3;
    }

    @Override
    public CraftingBookCategory category() {
        return category;
    }

    public ItemStack matches(CraftingInput inv1) {
        CraftingInputIndustrial inv = new CraftingInputIndustrial(inv1.width(), inv1.height(), inv1.items());
        int width = (int) inv.width();
        int height = inv.height();
        if (height == 1 && width == 1) {
            inv = new CraftingInputIndustrial(3, 1, List.of(inv.getItem(0), ItemStack.EMPTY, ItemStack.EMPTY));
        }
        if (height == 2 && width == 2) {
            inv = new CraftingInputIndustrial(3, 2, List.of(inv.getItem(0),inv.getItem(1), ItemStack.EMPTY,inv.getItem(2),inv.getItem(3), ItemStack.EMPTY));
        }
        if (height == 1 && width == 2) {
            inv = new CraftingInputIndustrial(3, 1, List.of(inv.getItem(0), inv.getItem(1), ItemStack.EMPTY));
        }
        if (height == 2 && width == 1) {
            inv = new CraftingInputIndustrial(1, 3, List.of(inv.getItem(0), inv.getItem(1), ItemStack.EMPTY));
        }
        width = (int) inv.width();
        height = inv.height();
        List<Map<Integer, ItemStack>> inventories = new ArrayList<>();

        if (width > 2 || height > 2) {

            if (width == 2) {
                Map<Integer, ItemStack> inventory = new HashMap<>();
                for (int j = 0; j < height; j++) {
                    inventory.put(j * 3, inv.getItem(j * 2));
                    inventory.put((j * 3) + 1, inv.getItem((j * 2) + 1));
                    inventory.put((j * 3) + 2, ItemStack.EMPTY);
                }
                inventories.add(new HashMap<>(inventory));
                inventory = new HashMap<>();
                for (int j = 0; j < height; j++) {
                    inventory.put(j * 3, ItemStack.EMPTY);
                    inventory.put((j * 3) + 1, inv.getItem(j * 2));
                    inventory.put((j * 3) + 2, inv.getItem((j * 2) + 1));
                }
                inventories.add(new HashMap<>(inventory));
            } else if (width == 1) {
                Map<Integer, ItemStack> inventory = new HashMap<>();
                for (int j = 0; j < height; j++) {
                    inventory.put(j * 3, ItemStack.EMPTY);
                    inventory.put((j * 3) + 1, inv.getItem(j));
                    inventory.put((j * 3) + 2, ItemStack.EMPTY);
                }
                inventories.add(new HashMap<>(inventory));
                inventory = new HashMap<>();
                for (int j = 0; j < height; j++) {
                    inventory.put(j * 3, ItemStack.EMPTY);
                    inventory.put((j * 3) + 1, ItemStack.EMPTY);
                    inventory.put((j * 3) + 2, inv.getItem(j));
                }
                inventories.add(new HashMap<>(inventory));
                inventory = new HashMap<>();
                for (int j = 0; j < height; j++) {
                    inventory.put(j * 3, inv.getItem(j));
                    inventory.put((j * 3) + 1, ItemStack.EMPTY);
                    inventory.put((j * 3) + 2, ItemStack.EMPTY);
                }
                inventories.add(new HashMap<>(inventory));

            } else if (height == 2) {
                Map<Integer, ItemStack> inventory = new HashMap<>();
                inventory.put(0, ItemStack.EMPTY);
                inventory.put(1, ItemStack.EMPTY);
                inventory.put(2, ItemStack.EMPTY);
                for (int j = 1; j < height + 1; j++) {
                    inventory.put(j * 3, inv.getItem((j - 1) * 3));
                    inventory.put((j * 3) + 1, inv.getItem(((j - 1) * 3) + 1));
                    inventory.put((j * 3) + 2, inv.getItem(((j - 1) * 3) + 2));
                }
                inventories.add(new HashMap<>(inventory));
                inventory = new HashMap<>();
                inventory.put(6, ItemStack.EMPTY);
                inventory.put(7, ItemStack.EMPTY);
                inventory.put(8, ItemStack.EMPTY);
                for (int j = 0; j < height; j++) {
                    inventory.put(j * 3, inv.getItem((j) * 3));
                    inventory.put((j * 3) + 1, inv.getItem(((j) * 3) + 1));
                    inventory.put((j * 3) + 2, inv.getItem(((j) * 3) + 2));
                }
                inventories.add(new HashMap<>(inventory));
            } else if (height == 1) {
                Map<Integer, ItemStack> inventory = new HashMap<>();
                inventory.put(0, ItemStack.EMPTY);
                inventory.put(1, ItemStack.EMPTY);
                inventory.put(2, ItemStack.EMPTY);
                for (int j = 1; j < height + 1; j++) {
                    inventory.put(j * 3, inv.getItem((j - 1) * 3));
                    inventory.put((j * 3) + 1, inv.getItem(((j - 1) * 3) + 1));
                    inventory.put((j * 3) + 2, inv.getItem(((j - 1) * 3) + 2));
                }
                inventory.put(6, ItemStack.EMPTY);
                inventory.put(7, ItemStack.EMPTY);
                inventory.put(8, ItemStack.EMPTY);
                inventories.add(new HashMap<>(inventory));
                inventory = new HashMap<>();
                inventory.put(3, ItemStack.EMPTY);
                inventory.put(4, ItemStack.EMPTY);
                inventory.put(5, ItemStack.EMPTY);
                inventory.put(6, ItemStack.EMPTY);
                inventory.put(7, ItemStack.EMPTY);
                inventory.put(8, ItemStack.EMPTY);
                for (int j = 0; j < height; j++) {
                    inventory.put(j * 3, inv.getItem((j) * 3));
                    inventory.put((j * 3) + 1, inv.getItem(((j) * 3) + 1));
                    inventory.put((j * 3) + 2, inv.getItem(((j) * 3) + 2));
                }
                inventories.add(new HashMap<>(inventory));
                inventory = new HashMap<>();
                inventory.put(0, ItemStack.EMPTY);
                inventory.put(1, ItemStack.EMPTY);
                inventory.put(2, ItemStack.EMPTY);
                inventory.put(3, ItemStack.EMPTY);
                inventory.put(4, ItemStack.EMPTY);
                inventory.put(5, ItemStack.EMPTY);
                for (int j = 2; j < height + 2; j++) {
                    inventory.put(j * 3, inv.getItem((j - 2) * 3));
                    inventory.put((j * 3) + 1, inv.getItem(((j - 2) * 3) + 1));
                    inventory.put((j * 3) + 2, inv.getItem(((j - 2) * 3) + 2));
                }
                inventories.add(new HashMap<>(inventory));
            }
            if (!inventories.isEmpty()) {
                for (Map<Integer, ItemStack> inventory : inventories) {
                    for (int j = 0; j < this.size; j++) {
                        if (j != this.size - 1) {
                            boolean has = true;
                            for (int i = 0; i < inventory.size(); i++) {
                                ItemStack offer = inventory.get(i);
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
                                if (input[j][i] instanceof InputOreDict && input[j][i].hasTag() &&  input[j][i].getInputs().isEmpty())
                                    input[j][i] = new InputOreDict(this.input[j][i].getTag(),this.input[j][i].getAmount());

                                if (!this.input[j][i].matches(offer)) {
                                    has = false;
                                    break;
                                }
                            }
                            if (has) {
                                return this.output.copy();
                            }
                        } else {
                            boolean has = true;
                            for (int i = 0; i < inventory.size(); i++) {
                                ItemStack offer = inventory.get(i);
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
                                if (input[j][i] instanceof InputOreDict && input[j][i].hasTag() &&  input[j][i].getInputs().isEmpty())
                                    input[j][i] = new InputOreDict(this.input[j][i].getTag(),this.input[j][i].getAmount());

                                if (!this.input[j][i].matches(offer)) {
                                    has = false;
                                    break;
                                }
                            }
                            if (has) {
                                return this.output.copy();
                            }
                        }
                    }
                }
            } else {
                for (int j = 0; j < this.size; j++) {
                    if (j != this.size - 1) {
                        boolean has = true;
                        for (int i = 0; i < 9; i++) {
                            ItemStack offer = inv.getItem(i % 3, i / 3);
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
                            if (input[j][i] instanceof InputOreDict && input[j][i].hasTag() &&  input[j][i].getInputs().isEmpty())
                                input[j][i] = new InputOreDict(this.input[j][i].getTag(),this.input[j][i].getAmount());

                            if (!this.input[j][i].matches(offer)) {
                                has = false;
                                break;
                            }
                        }
                        if (has) {
                            return this.output.copy();
                        }
                    } else {
                        for (int i = 0; i < width * height; i++) {
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
                            if (input[j][i] instanceof InputOreDict && input[j][i].hasTag() &&  input[j][i].getInputs().isEmpty())
                                input[j][i] = new InputOreDict(this.input[j][i].getTag(),this.input[j][i].getAmount());

                            if (!this.input[j][i].matches(offer)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                }
                return this.output.copy();
            }
        } else {
            return ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    public ShapedRecipePattern getPattern() {
        Map<Character, Ingredient> map = new HashMap<>();

        for (PartRecipe recipe : partRecipe) {
            Ingredient ingredient;
            if (recipe.getInput().getInputs().size() == 1 && !recipe.getInput().getInputs().get(0).getComponents().isEmpty()) {
                List<TypedDataComponent<?>> list = recipe.getInput().getInputs().get(0).getComponents().stream().collect(Collectors.toList());
                list.removeIf(typedDataComponent -> ModUtils.ignoredNbtKeys.contains(typedDataComponent.type()));
                if (!list.isEmpty()) {
                    HolderSet<Item> holders = HolderSet.direct(recipe.getInput().getInputs().get(0).getItemHolder());
                    ingredient = new Ingredient(new DataComponentIngredient(holders, DataComponentPredicate.allOf(recipe.getInput().getInputs().get(0).getComponents()), false));
                } else {
                    if (recipe.getInput() instanceof InputOreDict)
                        ingredient = Ingredient.of(recipe.getInput().getTag());
                    else
                        ingredient = (Ingredient.of(recipe.getInput().getInputs().toArray(new ItemStack[0])));
                }
            } else {
                if (recipe.getInput() instanceof InputOreDict)
                    ingredient = Ingredient.of(recipe.getInput().getTag());
                else
                    ingredient = (Ingredient.of(recipe.getInput().getInputs().toArray(new ItemStack[0])));
            }
            map.put(recipe.getIndex().charAt(0), ingredient);
        }
        return ShapedRecipePattern.of(map, recipeGrid.getArgs());
    }

    public void toNetwork(RegistryFriendlyByteBuf buf) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
        try {
            EncoderHandler.encode(packetBuffer, getGroup());
            EncoderHandler.encode(packetBuffer, category().ordinal());
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
