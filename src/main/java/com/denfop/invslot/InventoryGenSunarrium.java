package com.denfop.invslot;

import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryGenSunarrium extends Inventory {

    private final TileSolarGeneratorEnergy tile;
    private int stackSizeLimit;

    public InventoryGenSunarrium(TileSolarGeneratorEnergy base1) {
        super(base1, TypeItemSlot.INPUT, 4);
        this.stackSizeLimit = 1;
        this.tile = base1;
    }

    public boolean isItemValidForSlot(final int index, ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBaseModules && (itemStack.getItemDamage() < 6 || itemStack.getItemDamage() > 14);
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.lst = this.coefday();
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
