package com.denfop.render.rocketpad;

import com.denfop.IUItem;
import com.denfop.api.space.rovers.api.IRoversItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class DataRocket {

    private BlockPos pos;
    private ItemStack item;

    public DataRocket(IRoversItem roversItem, BlockPos pos){
        switch (roversItem.getLevel()){
            case ONE:
                item = new ItemStack(IUItem.rocket);
                break;
            case TWO:
                item = new ItemStack(IUItem.adv_rocket);
                break;
            case THREE:
                item = new ItemStack(IUItem.imp_rocket);
                break;
            case FOUR:
                item = new ItemStack(IUItem.per_rocket);
                break;
        }
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }

}
