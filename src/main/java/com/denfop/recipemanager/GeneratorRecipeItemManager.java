package com.denfop.recipemanager;

import com.denfop.api.IGeneratorRecipeItemmanager;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.IC2;
import ic2.core.init.MainConfig;
import ic2.core.recipe.RecipeInputItemStack;
import ic2.core.recipe.RecipeInputOreDict;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import ic2.core.util.Tuple;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class GeneratorRecipeItemManager implements IGeneratorRecipeItemmanager {

    private final Map<IRecipeInput, RecipeOutput> recipes = new HashMap();
    private final Map<Item, Map<Integer, Tuple.T2<IRecipeInput, RecipeOutput>>> recipeCache = new IdentityHashMap();
    private final List<Tuple.T2<IRecipeInput, RecipeOutput>> uncacheableRecipes = new ArrayList();
    private boolean oreRegisterEventSubscribed;

    public GeneratorRecipeItemManager() {
    }

    public void addRecipe(IRecipeInput input, Integer metadata, ItemStack... outputs) {

        if (!this.addRecipe(input, metadata, false, outputs)) {
            this.displayError("ambiguous recipe: [" + input.getInputs() + " -> " + Arrays.asList(outputs) + "]");
        }

    }

    public boolean addRecipe(IRecipeInput input, Integer metadata, boolean overwrite, ItemStack... outputs) {
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("amount", metadata);
        return this.addRecipe(input, new RecipeOutput(nbt, outputs), overwrite);
    }

    public RecipeOutput getOutputFor(ItemStack input, boolean adjustInput) {
        if (input == null) {
            return null;
        } else {
            Tuple.T2<IRecipeInput, RecipeOutput> data = this.getRecipe(input);
            if (data == null) {
                return null;
            } else if (input.getCount() < data.a.getAmount() || input
                    .getItem()
                    .hasContainerItem(input) && input.getCount() != data.a.getAmount()) {
                return null;
            } else {


                return data.b;
            }
        }
    }

    public Map<IRecipeInput, RecipeOutput> getRecipes() {
        return new AbstractMap<IRecipeInput, RecipeOutput>() {
            public Set<Entry<IRecipeInput, RecipeOutput>> entrySet() {
                return new AbstractSet<Entry<IRecipeInput, RecipeOutput>>() {
                    public Iterator<Entry<IRecipeInput, RecipeOutput>> iterator() {
                        return new Iterator<Entry<IRecipeInput, RecipeOutput>>() {
                            private final Iterator<Entry<IRecipeInput, RecipeOutput>> recipeIt;
                            private IRecipeInput lastInput;

                            {
                                this.recipeIt = GeneratorRecipeItemManager.this.recipes.entrySet().iterator();
                            }

                            public boolean hasNext() {
                                return this.recipeIt.hasNext();
                            }

                            public Entry<IRecipeInput, RecipeOutput> next() {
                                Entry<IRecipeInput, RecipeOutput> ret = this.recipeIt.next();
                                this.lastInput = ret.getKey();
                                return ret;
                            }

                            public void remove() {
                                this.recipeIt.remove();
                                GeneratorRecipeItemManager.this.removeCachedRecipes(this.lastInput);
                            }
                        };
                    }

                    public int size() {
                        return GeneratorRecipeItemManager.this.recipes.size();
                    }
                };
            }

            public RecipeOutput put(IRecipeInput key, RecipeOutput value) {
                GeneratorRecipeItemManager.this.addRecipe(key, value, true);
                return null;
            }
        };
    }

    @SubscribeEvent
    public void onOreRegister(OreDictionary.OreRegisterEvent event) {
        List<Tuple.T2<IRecipeInput, RecipeOutput>> datas = new ArrayList();
        Iterator var3 = this.recipes.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry<IRecipeInput, RecipeOutput> data = (Map.Entry) var3.next();
            if (data.getKey().getClass() == RecipeInputOreDict.class) {
                RecipeInputOreDict recipe = (RecipeInputOreDict) data.getKey();
                if (recipe.input.equals(event.getName())) {
                    datas.add(new Tuple.T2(data.getKey(), data.getValue()));
                }
            }
        }

        var3 = datas.iterator();

        while (var3.hasNext()) {
            Tuple.T2<IRecipeInput, RecipeOutput> data = (Tuple.T2) var3.next();
            this.addToCache(event.getOre(), data);
        }

    }

    private Tuple.T2<IRecipeInput, RecipeOutput> getRecipe(ItemStack input) {
        Map<Integer, Tuple.T2<IRecipeInput, RecipeOutput>> metaMap = this.recipeCache.get(input.getItem());
        if (metaMap != null) {
            Tuple.T2<IRecipeInput, RecipeOutput> data = metaMap.get(32767);
            if (data != null) {
                return data;
            }

            int meta = input.getItemDamage();
            data = metaMap.get(meta);
            if (data != null) {
                return data;
            }
        }

        Iterator var5 = this.uncacheableRecipes.iterator();

        Tuple.T2 data;
        do {
            if (!var5.hasNext()) {
                return null;
            }

            data = (Tuple.T2) var5.next();
        } while (!((IRecipeInput) data.a).matches(input));

        return data;
    }

    private boolean addRecipe(IRecipeInput input, RecipeOutput output, boolean overwrite) {
        if (input == null) {
            this.displayError("The recipe input is null");
            return false;
        } else {
            ListIterator it = output.items.listIterator();

            ItemStack is;
            while (it.hasNext()) {
                is = (ItemStack) it.next();
                if (is == null) {
                    this.displayError("An output ItemStack is null.");
                    return false;
                }

                if (!StackUtil.check(is)) {
                    this.displayError("The output ItemStack " + StackUtil.toStringSafe(is) + " is invalid.");
                    return false;
                }


                it.set(is.copy());
            }

            Iterator var7 = input.getInputs().iterator();

            while (true) {
                Tuple.T2 data;
                do {
                    if (!var7.hasNext()) {
                        this.recipes.put(input, output);
                        this.addToCache(input, output);
                        return true;
                    }

                    is = (ItemStack) var7.next();
                    data = this.getRecipe(is);
                } while (data == null);

                if (!overwrite) {
                    return false;
                }

                do {
                    this.recipes.remove(data.a);
                    this.removeCachedRecipes((IRecipeInput) data.a);
                    data = this.getRecipe(is);
                } while (data != null);
            }
        }
    }

    private void addToCache(IRecipeInput input, RecipeOutput output) {
        Tuple.T2<IRecipeInput, RecipeOutput> data = new Tuple.T2(input, output);
        List<ItemStack> stacks = this.getStacksFromRecipe(input);
        if (stacks != null) {

            for (ItemStack stack : stacks) {
                this.addToCache(stack, data);
            }

            if (input.getClass() == RecipeInputOreDict.class && !this.oreRegisterEventSubscribed) {
                MinecraftForge.EVENT_BUS.register(this);
                this.oreRegisterEventSubscribed = true;
            }
        } else {
            this.uncacheableRecipes.add(data);
        }

    }

    private void addToCache(ItemStack stack, Tuple.T2<IRecipeInput, RecipeOutput> data) {
        Item item = stack.getItem();
        Map<Integer, Tuple.T2<IRecipeInput, RecipeOutput>> metaMap = this.recipeCache.computeIfAbsent(item, k -> new HashMap());

        int meta = stack.getItemDamage();
        ((Map) metaMap).put(meta, data);
    }

    private void removeCachedRecipes(IRecipeInput input) {
        List<ItemStack> stacks = this.getStacksFromRecipe(input);
        Iterator it;
        if (stacks != null) {
            it = stacks.iterator();

            while (it.hasNext()) {
                ItemStack stack = (ItemStack) it.next();
                Item item = stack.getItem();
                int meta = stack.getItemDamage();
                Map<Integer, Tuple.T2<IRecipeInput, RecipeOutput>> map = this.recipeCache.get(item);
                if (map == null) {
                    IC2.log.warn(
                            LogCategory.Recipe,
                            "Inconsistent recipe cache, the entry for the item " + item + "(" + stack + ") is missing."
                    );
                } else {
                    map.remove(meta);
                    if (map.isEmpty()) {
                        this.recipeCache.remove(item);
                    }
                }
            }
        } else {
            it = this.uncacheableRecipes.iterator();

            while (it.hasNext()) {
                Tuple.T2<IRecipeInput, RecipeOutput> data = (Tuple.T2) it.next();
                if (data.a == input) {
                    it.remove();
                }
            }
        }

    }

    private List<ItemStack> getStacksFromRecipe(IRecipeInput recipe) {
        if (recipe.getClass() == RecipeInputItemStack.class) {
            return recipe.getInputs();
        } else if (recipe.getClass() == RecipeInputOreDict.class) {
            Integer meta = ((RecipeInputOreDict) recipe).meta;
            if (meta == null) {
                return recipe.getInputs();
            } else {
                List<ItemStack> ret = new ArrayList(recipe.getInputs());
                ListIterator it = ret.listIterator();

                while (it.hasNext()) {
                    ItemStack stack = (ItemStack) it.next();
                    if (stack.getItemDamage() != meta) {
                        stack = stack.copy();
                        stack.setItemDamage(meta);
                        it.set(stack);
                    }
                }

                return ret;
            }
        } else {
            return null;
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
