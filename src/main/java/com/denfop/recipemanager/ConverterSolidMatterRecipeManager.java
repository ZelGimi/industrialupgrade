//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.recipemanager;

import com.denfop.api.IUpgradeBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.recipe.RecipeOutput;
import ic2.core.IC2;
import ic2.core.init.MainConfig;
import ic2.core.recipe.MachineRecipeHelper;
import ic2.core.recipe.RecipeInputItemStack;
import ic2.core.recipe.RecipeInputOreDict;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class ConverterSolidMatterRecipeManager implements IUpgradeBasicMachineRecipeManager {

    public ConverterSolidMatterRecipeManager() {
    }

    protected final Map<IRecipeInput, MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipes = new HashMap();
    private final List<MachineRecipe<IRecipeInput, Collection<ItemStack>>> uncacheableRecipes = new ArrayList();
    private final Map<Item, List<MachineRecipe<IRecipeInput, Collection<ItemStack>>>> recipeCache = new IdentityHashMap();
    private static final Set<ConverterSolidMatterRecipeManager> watchingManagers =
            Collections.newSetFromMap(new IdentityHashMap());

    protected IRecipeInput getForInput(IRecipeInput input) {
        return input;
    }

    protected boolean consumeContainer(
            ItemStack input,
            ItemStack inContainer,
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe
    ) {
        Iterator var4 = ((Collection) recipe.getOutput()).iterator();

        ItemStack output;
        do {
            if (!var4.hasNext()) {
                return false;
            }

            output = (ItemStack) var4.next();
            if (StackUtil.checkItemEqualityStrict(inContainer, output)) {
                return true;
            }
        } while (!output.getItem().hasContainerItem(output) || !StackUtil.checkItemEqualityStrict(
                input,
                output.getItem().getContainerItem(output)
        ));

        return true;
    }

    public boolean addRecipe(IRecipeInput input, NBTTagCompound metadata, boolean replace, ItemStack... outputs) {
        return this.addRecipe(input, Arrays.asList(outputs), metadata, replace);
    }

    private void addToCache(Item item, MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe) {
        List<MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipes = this.recipeCache.get(item);
        if (recipes == null) {
            recipes = new ArrayList();
            this.recipeCache.put(item, recipes);
        }

        if (!recipes.contains(recipe)) {
            recipes.add(recipe);
        }

    }

    protected void addToCache(MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe) {
        Collection<Item> items = this.getItemsFromRecipe(recipe.getInput());
        if (items != null) {

            for (final Item item : items) {
                this.addToCache(item, recipe);
            }

            if (recipe.getInput().getClass() == RecipeInputOreDict.class) {
                if (!oreRegisterEventSubscribed) {
                    MinecraftForge.EVENT_BUS.register(MachineRecipeHelper.class);
                    oreRegisterEventSubscribed = true;
                }

                watchingManagers.add(this);
            }
        } else {
            this.uncacheableRecipes.add(recipe);
        }

    }

    private static boolean oreRegisterEventSubscribed;

    public boolean addRecipe(IRecipeInput input, Collection<ItemStack> output, NBTTagCompound metadata, boolean replace) {
        if (input == null) {
            throw new NullPointerException("null recipe input");
        } else if (output == null) {
            throw new NullPointerException("null recipe output");
        } else if (output.isEmpty()) {
            throw new IllegalArgumentException("no outputs");
        } else {
            ArrayList items = new ArrayList(output.size());
            Iterator var6 = output.iterator();

            while (true) {
                ItemStack is;
                if (var6.hasNext()) {
                    is = (ItemStack) var6.next();
                    if (StackUtil.isEmpty(is)) {
                        this.displayError("The output ItemStack " + StackUtil.toStringSafe(is) + " is invalid.");
                        return false;
                    }

                    if ((!input.matches(is) || input.matches(is)) || metadata != null && metadata.hasKey("ignoreSameInputOutput")) {
                        items.add(is.copy());
                        continue;
                    }

                    this.displayError("The output ItemStack " + is + " is the same as the recipe input " + input + ".");
                    return false;
                }

                var6 = input.getInputs().iterator();

                while (true) {
                    MachineRecipe recipe;
                    do {
                        if (!var6.hasNext()) {
                            recipe = new MachineRecipe(input, items, metadata);
                            this.recipes.put(input, recipe);
                            this.addToCache(recipe);
                            return true;
                        }

                        is = (ItemStack) var6.next();
                        recipe = this.getRecipe(is);
                    } while (recipe == null);

                    if (!replace) {
                        IC2.log.debug(
                                LogCategory.Recipe,
                                "Skipping %s => %s due to duplicate recipe for %s (%s => %s)",
                                input, output, is, recipe.getInput(), recipe.getOutput()
                        );
                        return false;
                    }

                    do {
                        this.recipes.remove(input);
                        this.removeCachedRecipes(input);
                        recipe = this.getRecipe(is);
                    } while (recipe != null);
                }
            }
        }
    }

    @Override
    public MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> apply(final ItemStack input, final boolean b) {
        if (StackUtil.isEmpty(input)) {
            return null;
        } else {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe = this.getRecipe(input);
            if (recipe == null) {
                return null;
            } else {
                IRecipeInput recipeInput = this.getForRecipe(recipe);
                if (StackUtil.getSize(input) < recipeInput.getAmount()) {
                    return null;
                } else {
                    ItemStack adjustedInput;
                    if (input.getItem().hasContainerItem(input) && !StackUtil.isEmpty(adjustedInput =
                            input.getItem().getContainerItem(input)) && !b && !this.consumeContainer(
                            input,
                            adjustedInput,
                            recipe
                    )) {
                        if (!b && StackUtil.getSize(input) != recipeInput.getAmount()) {
                            return null;
                        }

                        adjustedInput = StackUtil.copy(adjustedInput);
                    } else {
                        adjustedInput = StackUtil.copyWithSize(input, StackUtil.getSize(input) - recipeInput.getAmount());
                    }

                    return recipe.getResult(adjustedInput);
                }
            }
        }
    }

    @Override
    public Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> getRecipes() {
        return new Iterable<MachineRecipe<IRecipeInput, Collection<ItemStack>>>() {
            public Iterator<MachineRecipe<IRecipeInput, Collection<ItemStack>>> iterator() {
                return new Iterator<MachineRecipe<IRecipeInput, Collection<ItemStack>>>() {
                    private final Iterator<MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipeIt;
                    private IRecipeInput lastInput;

                    {
                        this.recipeIt = ConverterSolidMatterRecipeManager.this.recipes.values().iterator();
                    }

                    public boolean hasNext() {
                        return this.recipeIt.hasNext();
                    }

                    public MachineRecipe<IRecipeInput, Collection<ItemStack>> next() {
                        MachineRecipe<IRecipeInput, Collection<ItemStack>> next = this.recipeIt.next();
                        this.lastInput = next.getInput();
                        return next;
                    }

                    public void remove() {
                        this.recipeIt.remove();
                        ConverterSolidMatterRecipeManager.this.removeCachedRecipes(this.lastInput);
                    }
                };
            }
        };
    }

    private void removeCachedRecipes(IRecipeInput input) {
        Collection<Item> items = this.getItemsFromRecipe(input);
        if (items != null) {

            for (final Item item : items) {
                List recipes = this.recipeCache.get(item);
                if (recipes == null) {
                    IC2.log.warn(
                            LogCategory.Recipe,
                            "Inconsistent recipe cache, the entry for the item " + item + " is missing."
                    );
                } else {
                    this.removeInputFromRecipes(recipes.iterator(), input);
                    if (recipes.isEmpty()) {
                        this.recipeCache.remove(item);
                    }
                }
            }
        } else {
            this.removeInputFromRecipes(this.uncacheableRecipes.iterator(), input);
        }
    }

    private Collection<Item> getItemsFromRecipe(IRecipeInput recipe) {
        Class<?> recipeClass = recipe.getClass();
        if (recipeClass != RecipeInputItemStack.class && recipeClass != RecipeInputOreDict.class) {
            return null;
        } else {
            List<ItemStack> inputs = recipe.getInputs();
            Set<Item> ret = Collections.newSetFromMap(new IdentityHashMap(inputs.size()));

            for (final ItemStack stack : inputs) {
                ret.add(stack.getItem());
            }

            return ret;
        }
    }

    private void removeInputFromRecipes(
            Iterator<MachineRecipe<IRecipeInput, Collection<ItemStack>>> it,
            IRecipeInput target
    ) {
        assert target != null;

        while (it.hasNext()) {
            if (target.equals(((MachineRecipe) it.next()).getInput())) {
                it.remove();
            }
        }
    }

    @Override
    public boolean isIterable() {
        return false;
    }

    public RecipeOutput getOutputFor(ItemStack input, boolean adjustInput) {
        MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe = this.getRecipe(input);
        if (recipe == null) {
            return null;
        } else if (StackUtil.getSize(input) < recipe.getInput().getAmount() || input
                .getItem()
                .hasContainerItem(input) && StackUtil.getSize(input) != recipe.getInput().getAmount()) {
            return null;
        } else {
            if (adjustInput) {
                if (input.getItem().hasContainerItem(input)) {
                    throw new UnsupportedOperationException("can't adjust input item, use apply() instead");
                }

                input.shrink(recipe.getInput().getAmount());
            }

            return new RecipeOutput(recipe.getMetaData(), new ArrayList(recipe.getOutput()));
        }
    }

    private MachineRecipe<IRecipeInput, Collection<ItemStack>> getRecipe(ItemStack input) {
        if (StackUtil.isEmpty(input)) {
            return null;
        } else {
            List recipes = this.recipeCache.get(input.getItem());
            Iterator var3;
            MachineRecipe recipe;
            if (recipes != null) {
                var3 = recipes.iterator();

                while (var3.hasNext()) {
                    recipe = (MachineRecipe) var3.next();
                    if (this.getForRecipe(recipe).matches(input)) {
                        return recipe;
                    }
                }
            }

            var3 = this.uncacheableRecipes.iterator();

            do {
                if (!var3.hasNext()) {
                    return null;
                }

                recipe = (MachineRecipe) var3.next();
            } while (!this.getForRecipe(recipe).matches(input));

            return recipe;
        }
    }

    protected IRecipeInput getForRecipe(MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe) {
        return this.getForInput(recipe.getInput());
    }

    public void removeRecipe(ItemStack input, Collection<ItemStack> output) {
        MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe = this.getRecipe(input);
        if (recipe != null && checkListEquality(recipe.getOutput(), output)) {
            this.recipes.remove(recipe.getInput());
            this.removeCachedRecipes(recipe.getInput());
        }

    }

    private static boolean checkListEquality(Collection<ItemStack> a, Collection<ItemStack> b) {
        if (a.size() != b.size()) {
            return false;
        } else {
            ListIterator<ItemStack> itB = (new ArrayList(b)).listIterator();

            for (final ItemStack stack : a) {
                do {
                    if (!itB.hasNext()) {
                        return false;
                    }
                } while (!StackUtil.checkItemEqualityStrict(stack, itB.next()));

                itB.remove();

                while (itB.hasPrevious()) {
                    itB.previous();
                }
            }

            return true;
        }
    }

    private void displayError(String msg) {
        if (MainConfig.ignoreInvalidRecipes) {
            IC2.log.warn(LogCategory.Recipe, msg);
        } else {
            throw new RuntimeException(msg);
        }
    }

}
