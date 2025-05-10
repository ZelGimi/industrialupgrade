package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvSlotGenSunarrium extends InvSlot {

    private final TileSolarGeneratorEnergy tile;
    private int stackSizeLimit;

    public InvSlotGenSunarrium(TileSolarGeneratorEnergy base1) {
        super(base1, TypeItemSlot.INPUT, 4);
        this.stackSizeLimit = 1;
        this.tile = base1;
    }

    public boolean accepts(ItemStack itemStack, final int index) {
        return itemStack.getItem() instanceof ItemBaseModules && (IUItem.basemodules.getMeta((ItemBaseModules) itemStack.getItem()) < 6 || IUItem.basemodules.getMeta((ItemBaseModules) itemStack.getItem()) > 14);
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.tile.lst = this.coefday();
        return content;
    }

    public List<Double> coefday() {
        double coef = 0;
        double coef1 = 0;
        double coef2 = 0;
        List<Double> lst = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) this.get(i).getItem())) != null && this
                    .get(i)
                    .getItem() instanceof ItemBaseModules) {
                EnumModule module = EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) this.get(i).getItem()));
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
