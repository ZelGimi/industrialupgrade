package com.denfop.inventory;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.ReactorSchedule;
import com.denfop.items.ItemCraftingElements;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
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
            if (((ItemCraftingElements<?>) item).getElement().getId() == 143 && stack.has(DataComponentsInit.REACTOR_SCHEDULE)) {
                ReactorSchedule reactorSchedule = stack.get(DataComponentsInit.REACTOR_SCHEDULE);
                return type == reactorSchedule.type() && level == reactorSchedule.level();
            }
            return false;
        } else {
            return false;
        }
    }

    public void update() {
        this.accepts.clear();
        if (!this.get(0).isEmpty()) {
            ReactorSchedule reactorSchedule = this.get(0).get(DataComponentsInit.REACTOR_SCHEDULE);
            for (int y = 0; y < this.y; y++) {
                for (int x = 0; x < this.x; x++) {
                    ItemStack itemStack = reactorSchedule.items().get(y * this.x + x);
                    if (itemStack.isEmpty()) {
                        accepts.put(y * this.x + x, ItemStack.EMPTY);
                    } else {
                        accepts.put(y * this.x + x, itemStack);
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
            ReactorSchedule reactorSchedule = content.get(DataComponentsInit.REACTOR_SCHEDULE);
            for (int y = 0; y < this.y; y++) {
                for (int x = 0; x < this.x; x++) {
                    ItemStack itemStack = reactorSchedule.items().get(y * this.x + x);
                    if (itemStack.isEmpty()) {
                        accepts.put(y * this.x + x, ItemStack.EMPTY);
                    } else {
                        accepts.put(y * this.x + x, itemStack);
                    }
                }
            }
        }
        return content;
    }

}
