package com.denfop.items.bags;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerLeadBox;
import com.denfop.gui.GuiLeadBox;
import com.denfop.items.HandHeldInventory;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class HandHeldLeadBox extends HandHeldInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;


    public HandHeldLeadBox(EntityPlayer player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
    }

    public void save() {
        super.save();
    }

    public void saveAndThrow(ItemStack stack) {
        NBTTagList contentList = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!StackUtil.isEmpty(this.inventory[i])) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbt);
                contentList.appendTag(nbt);
            }
        }

        StackUtil.getOrCreateNbtData(stack).setTag("Items", contentList);
        this.clear();
    }

    public ContainerBase<HandHeldLeadBox> getGuiContainer(EntityPlayer player) {
        return new ContainerLeadBox(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiLeadBox(new ContainerLeadBox(player, this), itemStack1);
    }

    public ItemStack get(int index) {
        return this.inventory[index];
    }

    protected void restore(ItemStack[] backup) {
        if (backup.length != this.inventory.length) {
            throw new IllegalArgumentException("invalid array size");
        } else {
            System.arraycopy(backup, 0, this.inventory, 0, this.inventory.length);

        }
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }


    public int add(Collection<ItemStack> stacks) {
        return this.add(stacks, false);
    }

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
        }
    }

    public boolean canAdd(Collection<ItemStack> stacks) {
        return this.add(stacks, true) == 0;
    }

    public boolean canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), true);
        }
    }

    protected ItemStack[] backup() {
        ItemStack[] ret = new ItemStack[this.inventory.length];

        for (int i = 0; i < this.inventory.length; ++i) {
            ItemStack content = this.inventory[i];
            ret[i] = StackUtil.isEmpty(content) ? StackUtil.emptyStack : content.copy();
        }

        return ret;
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    public void put(int index, ItemStack content) {
        if (StackUtil.isEmpty(content)) {
            content = StackUtil.emptyStack;
        }

        this.inventory[index] = content;
        this.save();
    }

    public int getStackSizeLimit() {
        return 64;
    }

    private boolean add(List<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                for (int i = 0; i < this.inventory.length; i++) {
                    if (this.get(i) == null || this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.put(i, stack.copy());

                        }
                        return true;
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (stack.getTagCompound() != null &&
                                            stack.getTagCompound().equals(this.get(i).getTagCompound())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());

                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    private int add(Collection<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {
            ItemStack[] backup = simulate ? this.backup() : null;
            int totalAmount = 0;
            Iterator var5 = stacks.iterator();

            while (true) {
                ItemStack stack;
                int amount;
                do {
                    if (!var5.hasNext()) {
                        if (simulate) {
                            this.restore(backup);
                        }

                        return totalAmount;
                    }

                    stack = (ItemStack) var5.next();
                    amount = StackUtil.getSize(stack);
                } while (amount <= 0);

                label74:
                for (int pass = 0; pass < 2; ++pass) {
                    for (int i = 0; i < this.inventorySize; ++i) {
                        ItemStack existingStack = this.get(i);
                        int space = this.getStackSizeLimit();
                        if (!StackUtil.isEmpty(existingStack)) {
                            space = Math.min(space, existingStack.getMaxStackSize()) - StackUtil.getSize(existingStack);
                        }

                        if (space > 0) {
                            if (pass == 0 && !StackUtil.isEmpty(existingStack) && StackUtil.checkItemEqualityStrict(
                                    stack,
                                    existingStack
                            )) {
                                if (space >= amount) {
                                    this.put(i, StackUtil.incSize(existingStack, amount));
                                    amount = 0;
                                    break label74;
                                }

                                this.put(i, StackUtil.incSize(existingStack, space));
                                amount -= space;
                            } else if (pass == 1 && StackUtil.isEmpty(existingStack)) {
                                if (space >= amount) {
                                    this.put(i, StackUtil.copyWithSize(stack, amount));
                                    amount = 0;
                                    break label74;
                                }

                                this.put(i, StackUtil.copyWithSize(stack, space));
                                amount -= space;
                            }
                        }
                    }
                }

                totalAmount += amount;
            }
        } else {
            return 0;
        }
    }

}
