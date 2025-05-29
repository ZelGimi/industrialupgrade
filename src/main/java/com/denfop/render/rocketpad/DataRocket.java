package com.denfop.render.rocketpad;

import com.denfop.IUItem;
import com.denfop.api.space.rovers.api.IRoversItem;
import net.minecraft.world.item.ItemStack;

public class DataRocket {


    private double y;
    private ItemStack item;

    public DataRocket(IRoversItem roversItem, double y) {
        switch (roversItem.getLevel()) {
            case ONE:
                item = new ItemStack(IUItem.rocket.getItem());
                break;
            case TWO:
                item = new ItemStack(IUItem.adv_rocket.getItem());
                break;
            case THREE:
                item = new ItemStack(IUItem.imp_rocket.getItem());
                break;
            case FOUR:
                item = new ItemStack(IUItem.per_rocket.getItem());
                break;
        }
        this.y = y;
    }

    public double getPos() {
        return y;
    }

    public void setPos(final double pos) {
        this.y = pos;
    }

    public ItemStack getItem() {
        return item;
    }

}
