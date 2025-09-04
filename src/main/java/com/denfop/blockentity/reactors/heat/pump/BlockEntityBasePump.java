package com.denfop.blockentity.reactors.heat.pump;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.heat.IPump;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuHeatPump;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemCraftingElements;
import com.denfop.screen.ScreenHeatPump;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityBasePump extends BlockEntityMultiBlockElement implements IPump {

    private final int levelBlock;
    private final Inventory slot;
    private int power;
    private int energy;

    public BlockEntityBasePump(int levelBlock, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public int getStackSizeLimit() {
                return 1;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemCraftingElements)) {
                    return false;
                }
                final int itemDamage = ((ItemCraftingElements<?>) stack.getItem()).getElement().getId();
                switch (itemDamage) {
                    case 276:
                        return ((BlockEntityBasePump) this.base).getBlockLevel() >= 0;
                    case 20:
                        return ((BlockEntityBasePump) this.base).getBlockLevel() >= 1;
                    case 96:
                        return ((BlockEntityBasePump) this.base).getBlockLevel() >= 2;
                    case 120:
                        return ((BlockEntityBasePump) this.base).getBlockLevel() >= 3;
                }
                return false;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    ((BlockEntityBasePump) this.base).setEnergy(0);
                    ((BlockEntityBasePump) this.base).setPower(0);
                } else {
                    final int itemDamage = ((ItemCraftingElements<?>) content.getItem()).getElement().getId();
                    switch (itemDamage) {
                        case 276:
                            ((BlockEntityBasePump) this.base).setEnergy(5);
                            ((BlockEntityBasePump) this.base).setPower(1);
                            break;
                        case 20:
                            ((BlockEntityBasePump) this.base).setEnergy(10);
                            ((BlockEntityBasePump) this.base).setPower(2);
                            break;
                        case 96:
                            ((BlockEntityBasePump) this.base).setEnergy(20);
                            ((BlockEntityBasePump) this.base).setPower(3);
                            break;
                        case 120:
                            ((BlockEntityBasePump) this.base).setEnergy(40);
                            ((BlockEntityBasePump) this.base).setPower(4);
                            break;
                    }
                }
                return content;
            }
        };
    }

    @Override
    public ContainerMenuHeatPump getGuiContainer(final Player var1) {
        return new ContainerMenuHeatPump(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenHeatPump((ContainerMenuHeatPump) menu);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.getSlot().get(0).isEmpty()) {
                this.setEnergy(0);
                this.setPower(0);
            } else {
                final int itemDamage = ((ItemCraftingElements<?>) this.getSlot().get(0).getItem()).getElement().getId();
                switch (itemDamage) {
                    case 276:
                        this.setEnergy(5);
                        this.setPower(1);
                        break;
                    case 20:
                        this.setEnergy(10);
                        this.setPower(2);
                        break;
                    case 96:
                        this.setEnergy(20);
                        this.setPower(3);
                        break;
                    case 120:
                        this.setEnergy(40);
                        this.setPower(4);
                        break;
                }
            }
        }
    }


    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public int getBlockLevel() {
        return levelBlock;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    public int getPower() {
        return power;
    }

    public void setPower(final int power) {
        this.power = power;
    }

    public Inventory getSlot() {
        return slot;
    }

}
