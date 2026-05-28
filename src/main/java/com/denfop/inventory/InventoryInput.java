package com.denfop.inventory;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blockentity.base.BlockEntityAutoDigger;
import com.denfop.recipe.IInputHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class InventoryInput extends Inventory {

    private final BlockEntityAutoDigger tile;
    public ItemStack tool;

    public InventoryInput(BlockEntityAutoDigger tileEntityAutoDigger, int i) {
        super(tileEntityAutoDigger, TypeItemSlot.INPUT, i);
        this.tile = tileEntityAutoDigger;
        this.tool = new ItemStack(IUItem.drill.getItem());
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
        if (!this.get(index).isEmpty() && !this.tile.getWorld().isClientSide) {
            final Block block = Block.byItem(content.getItem());
            int chance = tile.chance;


            ResourceKey<Enchantment> fortune = Enchantments.FORTUNE;


            ItemEnchantments enchants =
                    tool.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

            int currentLevel = enchants.getLevel(this.tile.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(fortune));


            if (chance == 0) {
                if (currentLevel > 0) {

                    EnchantmentHelper.updateEnchantments(tool, (p_344404_) -> {
                        p_344404_.removeIf(holder -> holder.is(fortune));
                    });
                }

            }


            if (currentLevel != chance) {
                EnchantmentHelper.updateEnchantments(tool, (p_344404_) -> {
                    p_344404_.set(this.tile.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(fortune), chance);
                });
            }
            LootParams.Builder lootcontext$builder = (new LootParams.Builder((ServerLevel) this.tile.getWorld())).withLuck(tile.chance).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(new BlockPos(0, 0, 0))).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
            final List<ItemStack> list = new ArrayList<>(block.defaultBlockState().getDrops(lootcontext$builder));
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
            if (!this.get(i).isEmpty() && !this.tile.getWorld().isClientSide) {


                final Block block = Block.byItem(content.getItem());
                ResourceKey<Enchantment> fortune = Enchantments.FORTUNE;


                ItemEnchantments enchants =
                        tool.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

                int currentLevel = enchants.getLevel(this.tile.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(fortune));

                int chance = tile.chance;
                if (chance == 0) {
                    if (currentLevel > 0) {

                        EnchantmentHelper.updateEnchantments(tool, (p_344404_) -> {
                            p_344404_.removeIf(holder -> holder.is(fortune));
                        });
                    }

                }


                if (currentLevel != chance) {
                    EnchantmentHelper.updateEnchantments(tool, (p_344404_) -> {
                        p_344404_.set(this.tile.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(fortune), chance);
                    });
                }
                LootParams.Builder lootcontext$builder = (new LootParams.Builder((ServerLevel) this.tile.getWorld())).withLuck(tile.chance).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(new BlockPos(0, 0, 0))).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
                final List<ItemStack> list = new ArrayList<>(block.defaultBlockState().getDrops(lootcontext$builder));
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
            } else {
                this.tile.setBaseMachineRecipe(i, null);
            }
        }
    }

}
