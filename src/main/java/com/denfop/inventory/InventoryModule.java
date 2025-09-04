package com.denfop.inventory;


import com.denfop.IUItem;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.mechanism.BlockEntityModuleMachine;
import com.denfop.items.modules.ItemQuarryModule;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class InventoryModule extends Inventory implements ITypeSlot {

    private final int type;
    private final BlockEntityModuleMachine tile;
    private int stackSizeLimit;

    public InventoryModule(BlockEntityInventory base1, int type, int count) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, count);
        this.stackSizeLimit = 1;
        this.type = type;
        this.tile = (BlockEntityModuleMachine) base1;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (type == 0) {
            this.update();
        }
        return content;
    }

    public void update() {
        if (type == 0) {
            this.tile.listItems.clear();
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).isEmpty()) {

                    List<TagKey<Item>> list = this.get(i).getTags().toList();

                    for (TagKey<Item> ore : list)
                        if (ore.location().getPath().startsWith("ores/") || ore.location().getPath().startsWith("raw_materials/") || ore.location().getPath().startsWith("gems/") || ore.location().getPath().startsWith("ingots/") || ore.location().getPath().startsWith("dusts/") || ore.location().getPath().startsWith(
                                "shards/"))
                            if (!this.tile.listItems.contains(ore)) {
                                this.tile.listItems.add(ore);
                            }
                }

            }
        }
    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {
        if (type == 0) {
            if (!itemStack.getTags().toList().isEmpty()) {

                List<TagKey<Item>> ores = itemStack.getTags().toList();
                for (TagKey<Item> ore : ores)
                    if (ore.location().getPath().startsWith("ores/") || ore.location().getPath().startsWith("raw_materials/") || ore.location().getPath().startsWith("gems/") || ore.location().getPath().startsWith("ingots/") || ore.location().getPath().startsWith("dusts/") || ore.location().getPath().startsWith(
                            "shards/"))
                        return true;
                return false;
            } else {
                return false;
            }
        } else {
            return itemStack.getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) itemStack.getItem()) >= 12;
        }
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        switch (this.type) {
            case 0:
                return EnumTypeSlot.BLOCKS;
            case 1:
                return EnumTypeSlot.LIST;
        }
        return null;
    }

}
