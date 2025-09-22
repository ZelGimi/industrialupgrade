package com.denfop.tiles.reactors.heat.pump;

import com.denfop.container.ContainerHeatPump;
import com.denfop.gui.GuiHeatPump;
import com.denfop.invslot.Inventory;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.IPump;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBasePump extends TileEntityMultiBlockElement implements IPump {

    private final int level;
    private final Inventory slot;
    private int power;
    private int energy;

    public TileEntityBasePump(int level) {
        this.level = level;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public int getInventoryStackLimit() {
                return 1;
            }

            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemCraftingElements)) {
                    return false;
                }
                final int itemDamage = stack.getItemDamage();
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
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()) {
                    ((TileEntityBasePump) this.base).setEnergy(0);
                    ((TileEntityBasePump) this.base).setPower(0);
                } else {
                    final int itemDamage = content.getItemDamage();
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
            }
        };
    }

    @Override
    public ContainerHeatPump getGuiContainer(final EntityPlayer var1) {
        return new ContainerHeatPump(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiHeatPump(getGuiContainer(var1));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.getSlot().get().isEmpty()) {
                this.setEnergy(0);
                this.setPower(0);
            } else {
                final int itemDamage = this.getSlot().get().getItemDamage();
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
        return level;
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
