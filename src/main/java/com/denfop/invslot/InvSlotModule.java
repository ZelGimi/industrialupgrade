package com.denfop.invslot;


import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.TileModuleMachine;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class InvSlotModule extends InvSlot implements ITypeSlot {

    private final int type;
    private final TileModuleMachine tile;
    private int stackSizeLimit;

    public InvSlotModule(TileEntityInventory base1, int type, int count) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, count);
        this.stackSizeLimit = 1;
        this.type = type;
        this.tile = (TileModuleMachine) base1;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (type == 0) {
            this.update();
        }
    }

    public void update() {
        if (type == 0) {
            this.tile.listItems.clear();
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).isEmpty()) {


                    int id = OreDictionary.getOreIDs(this.get(i))[0];
                    String ore = OreDictionary.getOreName(id);

                    if (!this.tile.listItems.contains(ore)) {
                        this.tile.listItems.add(ore);
                    }
                }

            }
        }
    }

    public boolean accepts(ItemStack itemStack, final int index) {
        if (type == 0) {
            if (OreDictionary.getOreIDs(itemStack).length > 0) {
                int id = OreDictionary.getOreIDs(itemStack)[0];
                String ore = OreDictionary.getOreName(id);

                return ore.startsWith("ore") || ore.startsWith("gem") || ore.startsWith("ingot") || ore.startsWith("dust") || ore.startsWith(
                        "shard");
            } else {
                return false;
            }
        } else {
            return itemStack.getItem() instanceof ItemQuarryModule && itemStack.getItemDamage() >= 12;
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
