package com.denfop.tiles.reactors.gas.intercooler;

import com.denfop.container.ContainerInterCooler;
import com.denfop.gui.GuiInterCooler;
import com.denfop.invslot.Inventory;
import com.denfop.items.reactors.ItemsFan;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IInterCooler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBaseInterCooler extends TileEntityMultiBlockElement implements IInterCooler {

    private final int level;
    private final Inventory slot;
    private int power;
    private int energy;

    public TileEntityBaseInterCooler(int level) {
        this.level = level;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemsFan && ((ItemsFan) stack.getItem()).getLevel() <= ((TileEntityBaseInterCooler) this.base).getBlockLevel();
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (!world.isRemote) {
                    if (content.isEmpty()) {
                        ((TileEntityBaseInterCooler) this.base).setEnergy(0);
                        ((TileEntityBaseInterCooler) this.base).setPower(0);
                    } else {
                        ((TileEntityBaseInterCooler) this.base).setEnergy(((ItemsFan) content.getItem()).getEnergy());
                        ((TileEntityBaseInterCooler) this.base).setPower(((ItemsFan) content.getItem()).getPower());
                    }
                }
            }
        };
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            if (!this.getSlot().get().isEmpty() && ((ItemsFan) this.getSlot().get().getItem()).getDurabilityForDisplay(this
                    .getSlot()
                    .get()) == 1) {
                this.getSlot().put(0, ItemStack.EMPTY);
            }

        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.getSlot().get().isEmpty()) {
                this.setEnergy(0);
                this.setPower(0);
            } else {

                this.setEnergy(((ItemsFan) this.getSlot().get().getItem()).getEnergy());
                this.setPower(((ItemsFan) this.getSlot().get().getItem()).getPower());

            }
        }
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

    @Override
    public ContainerInterCooler getGuiContainer(final EntityPlayer var1) {
        return new ContainerInterCooler(this, var1);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiInterCooler(getGuiContainer(var1));
    }

}
