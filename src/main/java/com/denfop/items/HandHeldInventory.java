package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.inv.IHasGui;
import ic2.core.IC2;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.HashSet;
import java.util.Set;

public abstract class HandHeldInventory implements IHasGui {

    private static final Set<EntityPlayer> PLAYERS_IN_GUI = new HashSet<>();
    protected final ItemStack[] inventory;
    protected final EntityPlayer player;
    protected ItemStack containerStack;
    private boolean cleared;

    public HandHeldInventory(EntityPlayer player, ItemStack containerStack, int inventorySize) {
        this.containerStack = containerStack;
        this.inventory = new ItemStack[inventorySize];
        this.player = player;
        if (IC2.platform.isSimulating()) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(containerStack);
            if (!nbt.hasKey("uid", 3)) {
                nbt.setInteger("uid", IC2.random.nextInt());
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

    public static void addMaintainedPlayer(EntityPlayer player) {
        PLAYERS_IN_GUI.add(player);
    }

    public int getSizeInventory() {
        return this.inventory.length;
    }

    public boolean isEmpty() {
        ItemStack[] var1 = this.inventory;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ItemStack stack = var1[var3];
            if (!StackUtil.isEmpty(stack)) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int slot) {
        return StackUtil.wrapEmpty(this.inventory[slot]);
    }

    public ItemStack decrStackSize(int index, int amount) {
        ItemStack stack;
        if (index >= 0 && index < this.inventory.length && !StackUtil.isEmpty(stack = this.inventory[index])) {
            ItemStack ret;
            if (amount >= StackUtil.getSize(stack)) {
                ret = stack;
                this.inventory[index] = StackUtil.emptyStack;
            } else {
                ret = StackUtil.copyWithSize(stack, amount);
                this.inventory[index] = StackUtil.decSize(stack, amount);
            }

            this.save();
            return ret;
        } else {
            return StackUtil.emptyStack;
        }
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (!StackUtil.isEmpty(stack) && StackUtil.getSize(stack) > this.getInventoryStackLimit()) {
            stack = StackUtil.copyWithSize(stack, this.getInventoryStackLimit());
        }

        if (StackUtil.isEmpty(stack)) {
            this.inventory[slot] = StackUtil.emptyStack;
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
        if (!StackUtil.isEmpty(ret)) {
            this.setInventorySlotContents(index, null);
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

    public void onGuiClosed(EntityPlayer player) {
        this.save();
        if (!player.getEntityWorld().isRemote) {
            if (PLAYERS_IN_GUI.contains(player)) {
                PLAYERS_IN_GUI.remove(player);
            } else {
                StackUtil.getOrCreateNbtData(this.containerStack).removeTag("uid");
            }
        }

    }

    public boolean isThisContainer(ItemStack stack) {
        if (!StackUtil.isEmpty(stack) && stack.getItem() == this.containerStack.getItem()) {
            NBTTagCompound nbt = stack.getTagCompound();
            return nbt != null && nbt.getInteger("uid") == this.getUid();
        } else {
            return false;
        }
    }

    protected int getUid() {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(this.containerStack);
        return nbt.getInteger("uid");
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
        if (IC2.platform.isSimulating()) {
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
                    if (!StackUtil.isEmpty(this.inventory[idx])) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setByte("Slot", (byte) idx);
                        this.inventory[idx].writeToNBT(nbt);
                        contentList.appendTag(nbt);
                    }
                }

                StackUtil.getOrCreateNbtData(this.containerStack).setTag("Items", contentList);

                try {
                    this.containerStack = StackUtil.copyWithSize(this.containerStack, 1);
                } catch (IllegalArgumentException var6) {
                    CrashReport crash = new CrashReport("Hand held container stack vanished", var6);
                    CrashReportCategory category = crash.makeCategory("Container stack");
                    category.addCrashSection("Stack", StackUtil.toStringSafe(this.containerStack));
                    category.addCrashSection("NBT", this.containerStack.getTagCompound());
                    category.addCrashSection("Position", this.getPlayerInventoryIndex());
                    category.addCrashSection("Had thrown", dropItself);
                    category = crash.makeCategory("Container info");
                    category.addCrashSection("Type", this.getClass().getName());
                    category.addCrashSection(
                            "Container",
                            this.player.openContainer == null ? null : this.player.openContainer.getClass().getName()
                    );
                    if (this.player.world.isRemote) {
                        category.addDetail("GUI", () -> {
                            GuiScreen gui = Minecraft.getMinecraft().currentScreen;
                            return gui == null ? null : gui.getClass().getName();
                        });
                    }

                    category.addCrashSection("Opened by", this.player);
                    IUCore.log.info(crash);
                    return;
                }

                if (dropItself) {
                    StackUtil.dropAsEntity(this.player.getEntityWorld(), this.player.getPosition(), this.containerStack);
                    this.clear();
                } else {
                    idx = this.getPlayerInventoryIndex();
                    if (idx < -1) {
                        IC2.log.warn(
                                LogCategory.Item,
                                "Handheld inventory saving failed for player " + this.player
                                        .getDisplayName()
                                        .getUnformattedText() + '.'
                        );
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
        assert IC2.platform.isSimulating();

        NBTTagList contentList = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!StackUtil.isEmpty(this.inventory[i]) && !this.isThisContainer(this.inventory[i])) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbt);
                contentList.appendTag(nbt);
            }
        }

        StackUtil.getOrCreateNbtData(stack).setTag("Items", contentList);

        assert StackUtil.getOrCreateNbtData(stack).getInteger("uid") == 0;

        this.clear();
    }

    public void clear() {
        for (int i = 0; i < this.inventory.length; ++i) {
            this.inventory[i] = null;
        }

        this.cleared = true;
    }

    public void onEvent(String event) {
    }

}
