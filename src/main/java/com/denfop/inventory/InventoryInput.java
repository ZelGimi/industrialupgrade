package com.denfop.inventory;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blockentity.base.BlockEntityAutoDigger;
import com.denfop.recipe.IInputHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class InventoryInput extends Inventory {

    private final BlockEntityAutoDigger tile;

    public InventoryInput(BlockEntityAutoDigger tileEntityAutoDigger, int i) {
        super(tileEntityAutoDigger, TypeItemSlot.INPUT, i);
        this.tile = tileEntityAutoDigger;
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        Item item = stack.getItem();
        Block block = Block.byItem(item);


        return block != Blocks.AIR && !(block instanceof AirBlock) && !(block instanceof EntityBlock);

    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        final IInputHandler input = Recipes.inputFactory;
        if (!this.get(index).isEmpty()) {
            final Block block = Block.byItem(content.getItem());
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.tile.getWorld())).withLuck(tile.chance).withRandom(this.tile.getWorld().random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(new BlockPos(0, 0, 0))).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
            final List<ItemStack> list = new ArrayList<>(block.getDrops(block.defaultBlockState(), lootcontext$builder));
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
        return content;
    }

    public void update() {
        final IInputHandler input = Recipes.inputFactory;
        for (int i = 0; i < this.size(); i++) {
            final ItemStack content = this.get(i);
            if (content.isEmpty()) {
                this.tile.setBaseMachineRecipe(i, null);
                continue;
            }
            final Block block = Block.byItem(content.getItem());
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.tile.getWorld())).withLuck(tile.chance).withRandom(this.tile.getWorld().random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(new BlockPos(0, 0, 0))).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
            final List<ItemStack> list = new ArrayList<>(block.getDrops(block.defaultBlockState(), lootcontext$builder));
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
