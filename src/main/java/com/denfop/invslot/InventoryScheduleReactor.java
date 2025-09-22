package com.denfop.invslot;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.utils.ModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryScheduleReactor extends Inventory {

    private final int type;
    private final int level;
    private final int x;
    private final int y;
    Map<Integer, ItemStack> accepts = new HashMap<>();

    public InventoryScheduleReactor(final IAdvInventory<?> base, final int type, final int level, int x, int y) {
        super(base, TypeItemSlot.INPUT_OUTPUT, 1);
        this.type = type;
        this.level = level;
        this.x = x;
        this.y = y;
    }

    public Map<Integer, ItemStack> getAccepts() {
        return accepts;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemCraftingElements) {
            if (stack.getItemDamage() == 143) {
                final NBTTagCompound nbt = ModUtils.nbt(stack);
                return type == nbt.getInteger("type") && level == nbt.getInteger("level");
            }
            return false;
        } else {
            return false;
        }
    }

    public void update() {
        this.accepts.clear();
        if (!this.get().isEmpty()) {
            final NBTTagCompound nbt = ModUtils.nbt(this.get());
            NBTTagList nbtTagList = nbt.getTagList("Items", 10);
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < nbtTagList.tagCount(); ++i) {
                NBTTagCompound contentTag = nbtTagList.getCompoundTagAt(i);
                ItemStack stack = new ItemStack(contentTag);
                stackList.add(stack);
            }
            for (int y = 0; y < this.y; y++) {
                for (int x = 0; x < this.x; x++) {
                    NBTTagCompound tag = nbt.getCompoundTag(String.valueOf(y * this.x + x));
                    if (tag.getBoolean("empty")) {
                        accepts.put(y * this.x + x, ItemStack.EMPTY);
                    } else {
                        final int indexItem = tag.getInteger("index");
                        accepts.put(y * this.x + x, stackList.get(indexItem));
                    }
                }
            }
        }
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.accepts = new HashMap<>();
        if (!content.isEmpty()) {
            final NBTTagCompound nbt = ModUtils.nbt(content);
            NBTTagList nbtTagList = nbt.getTagList("Items", 10);
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < nbtTagList.tagCount(); ++i) {
                NBTTagCompound contentTag = nbtTagList.getCompoundTagAt(i);
                ItemStack stack = new ItemStack(contentTag);
                stackList.add(stack);
            }
            for (int y = 0; y < this.y; y++) {
                for (int x = 0; x < this.x; x++) {
                    NBTTagCompound tag = nbt.getCompoundTag(String.valueOf(y * this.x + x));
                    if (tag.getBoolean("empty")) {
                        accepts.put(y * this.x + x, ItemStack.EMPTY);
                    } else {
                        final int indexItem = tag.getInteger("index");
                        accepts.put(y * this.x + x, stackList.get(indexItem));
                    }
                }
            }
        }
    }

}
