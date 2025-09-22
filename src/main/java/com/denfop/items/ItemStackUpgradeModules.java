package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerUpgrade;
import com.denfop.gui.GUIUpgrade;
import com.denfop.invslot.Inventory;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemStackUpgradeModules extends ItemStackInventory {


    public final ItemStack itemStack1;
    public final ItemStack[] list;
    public final int inventorySize;
    public List<FluidStack> fluidStackList;

    public ItemStackUpgradeModules(EntityPlayer player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
        inventorySize = 0;
        this.list = new ItemStack[9];
        Arrays.fill(this.list, ItemStack.EMPTY);
        if (IUCore.proxy.isSimulating()) {
            NBTTagCompound nbt = ModUtils.nbt(containerStack);
            NBTTagList contentList = nbt.getTagList("Items1", 10);

            for (int i = 0; i < contentList.tagCount(); ++i) {
                NBTTagCompound slotNbt = contentList.getCompoundTagAt(i);
                int slot = slotNbt.getByte("Slot");
                if (slot >= 0 && slot < this.list.length) {
                    this.list[slot] = new ItemStack(slotNbt);
                }
            }
        }
        this.fluidStackList = new ArrayList<>(Collections.nCopies(9, null));
        if (!(IUItem.ejectorUpgrade.isItemEqual(itemStack1) || IUItem.pullingUpgrade.isItemEqual(itemStack1))) {
            for (int i = 0; i < 9; i++) {
                if (!this.list[i].isEmpty()) {
                    Block block = Block.getBlockFromItem(this.list[i].getItem());
                    if (block != Blocks.AIR) {
                        if (block instanceof IFluidBlock) {
                            fluidStackList.set(i, new FluidStack(((IFluidBlock) block).getFluid(), 1));
                        }
                    }
                }
            }
        }
    }

    public ContainerBase<ItemStackUpgradeModules> getGuiContainer(EntityPlayer player) {
        return new ContainerUpgrade(this);
    }

    public ItemStack getStackInSlot(int slot) {
        return slot >= inventorySize ? this.list[slot - inventorySize] : this.inventory[slot];
    }

    public ItemStack decrStackSize(int index, int amount) {
        if (index < this.inventory.length) {
            ItemStack stack;
            if (index >= 0 && index < this.inventory.length && !ModUtils.isEmpty(stack = this.inventory[index])) {
                ItemStack ret;
                if (amount >= ModUtils.getSize(stack)) {
                    ret = stack;
                    this.inventory[index] = ModUtils.emptyStack;
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.inventory[index] = ModUtils.decSize(stack, amount);
                }

                this.save();
                return ret;
            } else {
                return ModUtils.emptyStack;
            }
        } else {
            ItemStack stack;
            if (index - this.inventory.length < list.length && !ModUtils.isEmpty(stack =
                    this.list[index - this.inventory.length])) {
                ItemStack ret;
                if (amount >= ModUtils.getSize(stack)) {
                    ret = stack;
                    this.list[index - this.inventory.length] = ModUtils.emptyStack;
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.list[index - this.inventory.length] = ModUtils.decSize(stack, amount);
                }

                this.save();
                return ret;
            } else {
                return ModUtils.emptyStack;
            }
        }
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot < this.inventory.length) {
            if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
                stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
            }

            if (ModUtils.isEmpty(stack)) {
                this.inventory[slot] = ModUtils.emptyStack;
            } else {
                this.inventory[slot] = stack;
            }
        } else {
            if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
                stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
            }

            if (ModUtils.isEmpty(stack)) {
                this.list[slot - this.inventory.length] = ModUtils.emptyStack;
            } else {
                this.list[slot - this.inventory.length] = stack;
            }
        }
        this.save();
    }

    protected void save() {
        super.save();
        if (IUCore.proxy.isSimulating()) {
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.list.length; ++i) {
                    if (this.isThisContainer(this.list[i])) {
                        this.list[i] = null;
                        dropItself = true;
                    }
                }

                NBTTagList contentList = new NBTTagList();

                int idx;
                for (idx = 0; idx < this.list.length; ++idx) {
                    if (!ModUtils.isEmpty(this.list[idx])) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setByte("Slot", (byte) idx);
                        this.list[idx].writeToNBT(nbt);
                        contentList.appendTag(nbt);
                    }
                }

                ModUtils.nbt(this.containerStack).setTag("Items1", contentList);

                this.containerStack = ModUtils.setSize(this.containerStack, 1);

                if (dropItself) {
                    ModUtils.dropAsEntity(this.player.getEntityWorld(), this.player.getPosition(), this.containerStack);
                    this.clear();
                } else {
                    idx = this.getPlayerInventoryIndex();
                    if (idx < -1) {
                        this.clear();
                    } else if (idx == -1) {
                        this.player.inventory.setItemStack(this.containerStack);
                    } else {
                        this.player.inventory.setInventorySlotContents(idx, this.containerStack);
                    }
                }

            }
        }
    }

    public ItemStack[] getList() {
        return list;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        if (this.itemStack1.isItemEqual(IUItem.ejectorUpgrade) || this.itemStack1.isItemEqual(IUItem.pullingUpgrade)) {
            return new GUIUpgrade(new ContainerUpgrade(this), itemStack1);
        } else {
            return new GUIUpgrade(new ContainerUpgrade(this), itemStack1);
        }
    }

    @Override
    public void addInventorySlot(final Inventory var1) {

    }


    @Nonnull
    public String getName() {
        return "";
    }

    public boolean hasCustomName() {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

}
