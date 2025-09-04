package com.denfop.inventory;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.items.ItemCraftingElements;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

    public InventoryScheduleReactor(final CustomWorldContainer base, final int type, final int level, int x, int y) {
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
    public int getStackSizeLimit() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemCraftingElements) {
            if (((ItemCraftingElements<?>) item).getElement().getId() == 143) {
                final CompoundTag nbt = ModUtils.nbt(stack);
                return type == nbt.getInt("type") && level == nbt.getInt("level");
            }
            return false;
        } else {
            return false;
        }
    }

    public void update() {
        this.accepts.clear();
        if (!this.get(0).isEmpty()) {
            final CompoundTag nbt = ModUtils.nbt(this.get(0));
            ListTag nbtTagList = nbt.getList("Items", 10);
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < nbtTagList.size(); ++i) {
                CompoundTag contentTag = nbtTagList.getCompound(i);
                ItemStack stack = ItemStack.of(contentTag);
                stackList.add(stack);
            }
            for (int y = 0; y < this.y; y++) {
                for (int x = 0; x < this.x; x++) {
                    CompoundTag tag = nbt.getCompound(String.valueOf(y * this.x + x));
                    if (tag.getBoolean("empty")) {
                        accepts.put(y * this.x + x, ItemStack.EMPTY);
                    } else {
                        final int indexItem = tag.getInt("index");
                        accepts.put(y * this.x + x, stackList.get(indexItem));
                    }
                }
            }
        }
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.accepts = new HashMap<>();
        if (!content.isEmpty()) {
            final CompoundTag nbt = ModUtils.nbt(content);
            ListTag nbtTagList = nbt.getList("Items", 10);
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < nbtTagList.size(); ++i) {
                CompoundTag contentTag = nbtTagList.getCompound(i);
                ItemStack stack = ItemStack.of(contentTag);
                stackList.add(stack);
            }
            for (int y = 0; y < this.y; y++) {
                for (int x = 0; x < this.x; x++) {
                    CompoundTag tag = nbt.getCompound(String.valueOf(y * this.x + x));
                    if (tag.getBoolean("empty")) {
                        accepts.put(y * this.x + x, ItemStack.EMPTY);
                    } else {
                        final int indexItem = tag.getInt("index");
                        accepts.put(y * this.x + x, stackList.get(indexItem));
                    }
                }
            }
        }
        return content;
    }

}
