package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.tiles.base.TileEntityCombinerSEGenerators;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryGenCombinerSunarrium extends Inventory implements ITypeSlot {

    private final TileEntityCombinerSEGenerators tile;
    private int stackSizeLimit;

    public InventoryGenCombinerSunarrium(TileEntityCombinerSEGenerators base1) {
        super(base1, TypeItemSlot.INPUT, 4);
        this.stackSizeLimit = 1;
        this.tile = base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.QUARRY1;
    }

    public boolean isItemValidForSlot(final int index, ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBaseModules && (itemStack.getItemDamage() < 6 || itemStack.getItemDamage() > 14);
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.lst = this.coefday();

        this.tile.coef_day = this.tile.lst.get(0);
        this.tile.coef_night = this.tile.lst.get(1);
        this.tile.update_night = this.tile.lst.get(2);
    }

    public List<Double> coefday() {
        double coef = 0;
        double coef1 = 0;
        double coef2 = 0;
        List<Double> lst = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && EnumModule.getFromID(this.get(i).getItemDamage()) != null && this
                    .get(i)
                    .getItem() instanceof ItemBaseModules) {
                EnumModule module = EnumModule.getFromID(this.get(i).getItemDamage());
                EnumBaseType type = module.type;
                double percent = module.percent;
                switch (type) {
                    case DAY:
                        coef += percent;
                        break;
                    case NIGHT:
                        coef1 += percent;
                        break;
                    case MOON_LINSE:
                        coef2 = percent;
                        break;
                }
            }
        }
        lst.add(coef);
        lst.add(coef1);
        lst.add(coef2);
        return lst;
    }

    public int getInventoryStackLimit() {
        return this.stackSizeLimit;
    }

    public void setInventoryStackLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
