package com.denfop.items.crop;

import com.denfop.api.agriculture.ICrop;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.container.ContainerAgriculturalAnalyzer;
import com.denfop.gui.GuiAgriculturalAnalyzer;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.utils.ModUtils;
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

public class ItemStackAgriculturalAnalyzer extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;
    public Genome genome;
    public ICrop crop;


    public ItemStackAgriculturalAnalyzer(EntityPlayer player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {

        return itemstack.getItem() instanceof ICropItem;
    }

    public void save() {
        super.save();
    }

    public void saveAndThrow(ItemStack stack) {
        NBTTagList contentList = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!ModUtils.isEmpty(this.inventory[i])) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbt);
                contentList.appendTag(nbt);
            }
        }

        ModUtils.nbt(stack).setTag("Items", contentList);
        this.clear();
    }

    public ContainerAgriculturalAnalyzer getGuiContainer(EntityPlayer player) {
        return new ContainerAgriculturalAnalyzer(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAgriculturalAnalyzer(getGuiContainer(player), itemStack1);
    }


    @Override
    public void addInventorySlot(final Inventory var1) {

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
            ret[i] = ModUtils.isEmpty(content) ? ModUtils.emptyStack : content.copy();
        }

        return ret;
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    public void put(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
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
                    amount = ModUtils.getSize(stack);
                } while (amount <= 0);

                label74:
                for (int pass = 0; pass < 2; ++pass) {
                    for (int i = 0; i < this.inventorySize; ++i) {
                        ItemStack existingStack = this.get(i);
                        int space = this.getStackSizeLimit();
                        if (!ModUtils.isEmpty(existingStack)) {
                            space = Math.min(space, existingStack.getMaxStackSize()) - ModUtils.getSize(existingStack);
                        }

                        if (space > 0) {
                            if (pass == 0 && !ModUtils.isEmpty(existingStack) && ModUtils.checkItemEqualityStrict(
                                    stack,
                                    existingStack
                            )) {
                                if (space >= amount) {
                                    existingStack.grow(amount);
                                    this.put(i, existingStack);
                                    amount = 0;
                                    break label74;
                                }
                                existingStack.grow(space);
                                this.put(i, existingStack);
                                amount -= space;
                            } else if (pass == 1 && ModUtils.isEmpty(existingStack)) {
                                if (space >= amount) {
                                    this.put(i, ModUtils.setSize(stack, amount));
                                    amount = 0;
                                    break label74;
                                }

                                this.put(i, ModUtils.setSize(stack, space));
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
