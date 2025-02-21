package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;

public abstract class ItemStackInventory implements IAdvInventory, IUpdatableItemStack {

    public final EntityPlayer player;
    protected final ItemStack[] inventory;
    protected ItemStack containerStack;
    protected boolean cleared;

    public ItemStackInventory(EntityPlayer player, ItemStack containerStack, int inventorySize) {
        this.containerStack = containerStack;
        this.inventory = new ItemStack[inventorySize];
        Arrays.fill(this.inventory, ItemStack.EMPTY);
        this.player = player;
        if (IUCore.proxy.isSimulating()) {
            NBTTagCompound nbt = ModUtils.nbt(containerStack);
            if (!nbt.hasKey("uid", 3)) {
                nbt.setInteger("uid", IUCore.random.nextInt());
            }

            NBTTagList contentList = nbt.getTagList("Items", 10);

            for (int i = 0; i < contentList.tagCount(); ++i) {
                NBTTagCompound slotNbt = contentList.getCompoundTagAt(i);
                int slot = slotNbt.getByte("Slot");
                if (slot >= 0 && slot < this.inventory.length) {
                    this.inventory[slot] = new ItemStack(slotNbt);
                }
            }
        }

    }

    @Override
    public CustomPacketBuffer writeContainer() {
        return new CustomPacketBuffer();
    }

    @Override
    public void readContainer(final CustomPacketBuffer buffer) {

    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public int getSizeInventory() {
        return this.inventory.length;
    }

    public boolean isEmpty() {

        for (ItemStack stack : this.inventory) {
            if (!ModUtils.isEmpty(stack)) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getContainerStack() {
        return containerStack;
    }

    public ItemStack getStackInSlot(int slot) {
        return this.inventory[slot];
    }

    public ItemStack decrStackSize(int index, int amount) {
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
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
            stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
        }

        if (ModUtils.isEmpty(stack)) {
            this.inventory[slot] = ModUtils.emptyStack;
        } else {
            this.inventory[slot] = stack;
        }

        this.save();
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack1) {
        return false;
    }

    public void markDirty() {
        this.save();
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return player == this.player && this.getPlayerInventoryIndex() >= -1;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }

    public ItemStack removeStackFromSlot(int index) {
        ItemStack ret = this.getStackInSlot(index);
        if (!ModUtils.isEmpty(ret)) {
            this.setInventorySlotContents(index, ItemStack.EMPTY);
        }

        return ret;
    }

    public int getField(int id) {
        return 0;
    }

    public void setField(int id, int value) {
    }

    public int getFieldCount() {
        return 0;
    }

    public ITextComponent getDisplayName() {
        return new TextComponentString(this.getName());
    }

    public boolean isThisContainer(ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && stack.getItem() == this.containerStack.getItem()) {
            NBTTagCompound nbt = stack.getTagCompound();
            return nbt != null && nbt.getInteger("uid") == this.getUid();
        } else {
            return false;
        }
    }

    protected int getUid() {
        NBTTagCompound nbt = ModUtils.nbt(this.containerStack);
        return nbt.getInteger("uid");
    }

    public NBTTagCompound getNBT() {
        NBTTagCompound nbt = ModUtils.nbt(this.containerStack);
        return nbt;
    }

    protected int getPlayerInventoryIndex() {
        for (int i = -1; i < this.player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = i == -1 ? this.player.inventory.getItemStack() : this.player.inventory.getStackInSlot(i);
            if (this.isThisContainer(stack)) {
                return i;
            }
        }

        return -2147483648;
    }

    protected void save() {
        if (IUCore.proxy.isSimulating()) {
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.inventory.length; ++i) {
                    if (this.isThisContainer(this.inventory[i])) {
                        this.inventory[i] = null;
                        dropItself = true;
                    }
                }

                NBTTagList contentList = new NBTTagList();

                int idx;
                for (idx = 0; idx < this.inventory.length; ++idx) {
                    if (!ModUtils.isEmpty(this.inventory[idx])) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setByte("Slot", (byte) idx);
                        this.inventory[idx].writeToNBT(nbt);
                        contentList.appendTag(nbt);
                    }
                }

                ModUtils.nbt(this.containerStack).setTag("Items", contentList);

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

    public void saveAsThrown(ItemStack stack) {
        assert IUCore.proxy.isSimulating();

        NBTTagList contentList = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!ModUtils.isEmpty(this.inventory[i]) && !this.isThisContainer(this.inventory[i])) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbt);
                contentList.appendTag(nbt);
            }
        }

        ModUtils.nbt(stack).setTag("Items", contentList);

        assert ModUtils.nbt(stack).getInteger("uid") == 0;

        this.clear();
    }

    public void clear() {
        Arrays.fill(this.inventory, ItemStack.EMPTY);

        this.cleared = true;
    }

}
