package com.denfop.invslot;

import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.tiles.base.TileEntityCombinerSEGenerators;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvSlotGenCombinerSunarrium extends InvSlot {

    private final TileEntityCombinerSEGenerators tile;
    private int stackSizeLimit;

    public InvSlotGenCombinerSunarrium(TileEntityCombinerSEGenerators base1) {
        super(base1, "input", InvSlot.Access.I, 4, InvSlot.InvSide.TOP);
        this.stackSizeLimit = 1;
        this.tile = base1;
    }

    public boolean accepts(ItemStack itemStack) {
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

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
