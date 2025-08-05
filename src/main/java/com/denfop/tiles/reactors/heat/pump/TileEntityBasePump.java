package com.denfop.tiles.reactors.heat.pump;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerHeatPump;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiHeatPump;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemCraftingElements;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.IPump;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityBasePump extends TileEntityMultiBlockElement implements IPump {

    private final int levelBlock;
    private final InvSlot slot;
    private int power;
    private int energy;

    public TileEntityBasePump(int levelBlock, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public int getStackSizeLimit() {
                return 1;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (!(stack.getItem() instanceof ItemCraftingElements)) {
                    return false;
                }
                final int itemDamage = ((ItemCraftingElements<?>) stack.getItem()).getElement().getId();
                switch (itemDamage) {
                    case 276:
                        return ((TileEntityBasePump) this.base).getBlockLevel() >= 0;
                    case 20:
                        return ((TileEntityBasePump) this.base).getBlockLevel() >= 1;
                    case 96:
                        return ((TileEntityBasePump) this.base).getBlockLevel() >= 2;
                    case 120:
                        return ((TileEntityBasePump) this.base).getBlockLevel() >= 3;
                }
                return false;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    ((TileEntityBasePump) this.base).setEnergy(0);
                    ((TileEntityBasePump) this.base).setPower(0);
                } else {
                    final int itemDamage = ((ItemCraftingElements<?>) content.getItem()).getElement().getId();
                    switch (itemDamage) {
                        case 276:
                            ((TileEntityBasePump) this.base).setEnergy(5);
                            ((TileEntityBasePump) this.base).setPower(1);
                            break;
                        case 20:
                            ((TileEntityBasePump) this.base).setEnergy(10);
                            ((TileEntityBasePump) this.base).setPower(2);
                            break;
                        case 96:
                            ((TileEntityBasePump) this.base).setEnergy(20);
                            ((TileEntityBasePump) this.base).setPower(3);
                            break;
                        case 120:
                            ((TileEntityBasePump) this.base).setEnergy(40);
                            ((TileEntityBasePump) this.base).setPower(4);
                            break;
                    }
                }
                return content;
            }
        };
    }

    @Override
    public ContainerHeatPump getGuiContainer(final Player var1) {
        return new ContainerHeatPump(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiHeatPump((ContainerHeatPump) menu);
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

    public InvSlot getSlot() {
        return slot;
    }

}
