package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileEntityAutoDigger;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InventoryInput extends Inventory {

    private final TileEntityAutoDigger tile;

    public InventoryInput(TileEntityAutoDigger tileEntityAutoDigger, int i) {
        super(tileEntityAutoDigger, TypeItemSlot.INPUT, i);
        this.tile = tileEntityAutoDigger;
    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        final Block block = Block.getBlockFromItem(stack.getItem());
        return block != Blocks.AIR;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        final IInputHandler input = Recipes.inputFactory;
        if (!this.get(index).isEmpty()) {
            final Block block = Block.getBlockFromItem(content.getItem());
            final List<ItemStack> list = block.getDrops(this.tile.getWorld(), new BlockPos(0, 0, 0),
                    block.getStateFromMeta(content.getItemDamage()), this.tile.chance
            );
            if (block == Blocks.IRON_ORE || block == Blocks.GOLD_ORE) {
                list.clear();
                if (block == Blocks.GOLD_ORE) {
                    list.add(new ItemStack(IUItem.rawMetals, Math.min(
                            4,
                            1 + tile.getWorld().rand.nextInt(Math.min(4, Math.max(1, this.tile.chance)))
                    )
                            , 17));
                }
                if (block == Blocks.IRON_ORE) {
                    list.add(new ItemStack(IUItem.rawMetals, Math.min(
                            4,
                            1 + tile.getWorld().rand.nextInt(Math.min(4, Math.max(1, this.tile.chance)))
                    )
                            , 18));
                }
            }

            if (this.tile.comb_mac_enabled) {
                final List<ItemStack> list1 = new ArrayList<>();
                final List<ItemStack> list2 = new ArrayList<>();
                for (ItemStack stack : list) {
                    final BaseMachineRecipe recipe = com.denfop.api.Recipes.recipes.getRecipeOutput(
                            "comb_macerator",
                            false,
                            stack
                    );
                    if (recipe == null) {
                        continue;
                    }
                    list1.add(stack);
                    recipe.getOutput().items.forEach(stack1 -> {
                        stack1 = stack1.copy();
                        stack1.setCount(stack1.getCount() * (stack.getCount() / recipe.input.getInputs().get(0).getAmount()));
                        list2.add(stack1);
                    });
                }
                list.removeAll(list1);
                list.addAll(list2);
            } else if (this.tile.mac_enabled) {
                final List<ItemStack> list1 = new ArrayList<>();
                final List<ItemStack> list2 = new ArrayList<>();
                for (ItemStack stack : list) {
                    final BaseMachineRecipe recipe = com.denfop.api.Recipes.recipes.getRecipeOutput("macerator", false, stack);
                    if (recipe == null) {
                        continue;
                    }
                    list1.add(stack);
                    recipe.getOutput().items.forEach(stack1 -> {
                        stack1 = stack1.copy();
                        stack1.setCount(stack1.getCount() * (stack.getCount() / recipe.input.getInputs().get(0).getAmount()));
                        list2.add(stack1);
                    });
                }
                list.removeAll(list1);
                list.addAll(list2);

            }

            if (this.tile.furnace) {
                final List<ItemStack> list1 = new ArrayList<>();
                final List<ItemStack> list2 = new ArrayList<>();
                for (ItemStack stack : list) {
                    final BaseMachineRecipe recipe = com.denfop.api.Recipes.recipes.getRecipeOutput("furnace", false, stack);
                    if (recipe == null) {
                        continue;
                    }
                    list1.add(stack);
                    final List<ItemStack> list3 = new ArrayList<>();
                    recipe.getOutput().items.forEach(stack1 -> {
                        stack1 = stack1.copy();
                        stack1.setCount(stack1.getCount() * (stack.getCount() / recipe.input.getInputs().get(0).getAmount()));
                        list3.add(stack1);
                    });
                    list3.forEach(stack1 -> stack1.setCount(stack.getCount()));

                    list2.addAll(list3);
                }
                list.removeAll(list1);
                list.addAll(list2);
            }
            this.tile.setBaseMachineRecipe(index, new BaseMachineRecipe(
                    new Input(input.getInput(this.get(index))),
                    new RecipeOutput(null, list)
            ));
        } else {
            this.tile.setBaseMachineRecipe(index, null);
        }
    }

    public void update() {
        final IInputHandler input = Recipes.inputFactory;
        for (int i = 0; i < this.size(); i++) {
            final ItemStack content = this.get(i);
            if (content.isEmpty()) {
                this.tile.setBaseMachineRecipe(i, null);
                continue;
            }
            final Block block = Block.getBlockFromItem(content.getItem());
            final List<ItemStack> list = block.getDrops(this.tile.getWorld(), new BlockPos(0, 0, 0),
                    block.getStateFromMeta(content.getItemDamage()), this.tile.chance
            );
            if (this.tile.comb_mac_enabled) {
                final List<ItemStack> list1 = new ArrayList<>();
                final List<ItemStack> list2 = new ArrayList<>();
                for (ItemStack stack : list) {
                    final BaseMachineRecipe recipe = com.denfop.api.Recipes.recipes.getRecipeOutput(
                            "comb_macerator",
                            false,
                            stack
                    );
                    if (recipe == null) {
                        continue;
                    }
                    list1.add(stack);
                    recipe.getOutput().items.forEach(stack1 -> {
                        stack1 = stack1.copy();
                        stack1.setCount(stack1.getCount() * (stack.getCount() / recipe.input.getInputs().get(0).getAmount()));
                        list2.add(stack1);
                    });
                }
                list.removeAll(list1);
                list.addAll(list2);
            } else if (this.tile.mac_enabled) {
                final List<ItemStack> list1 = new ArrayList<>();
                final List<ItemStack> list2 = new ArrayList<>();
                for (ItemStack stack : list) {

                    final BaseMachineRecipe recipe = com.denfop.api.Recipes.recipes.getRecipeOutput("macerator", false, stack);
                    if (recipe == null) {
                        continue;
                    }
                    list1.add(stack);
                    recipe.getOutput().items.forEach(stack1 -> {
                        stack1 = stack1.copy();
                        stack1.setCount(stack1.getCount() * (stack.getCount() / recipe.input.getInputs().get(0).getAmount()));
                        list2.add(stack1);
                    });
                }
                list.removeAll(list1);
                list.addAll(list2);

            }

            if (this.tile.furnace) {
                final List<ItemStack> list1 = new ArrayList<>();
                final List<ItemStack> list2 = new ArrayList<>();

                for (ItemStack stack : list) {
                    final BaseMachineRecipe recipe = com.denfop.api.Recipes.recipes.getRecipeOutput("furnace", false, stack);
                    if (recipe == null) {
                        continue;
                    }
                    list1.add(stack);
                    recipe.getOutput().items.forEach(stack1 -> {
                        stack1 = stack1.copy();
                        stack1.setCount(stack1.getCount() * (stack.getCount() / recipe.input.getInputs().get(0).getAmount()));
                        list2.add(stack1);
                    });
                }
                list.removeAll(list1);
                list.addAll(list2);
            }

            this.tile.setBaseMachineRecipe(i, new BaseMachineRecipe(
                    new Input(input.getInput(this.get(i))),
                    new RecipeOutput(null, list)
            ));
        }
    }

}
