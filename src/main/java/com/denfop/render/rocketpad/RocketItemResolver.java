package com.denfop.render.rocketpad;

import com.denfop.IUItem;
import com.denfop.api.space.rovers.api.IRoversItem;
import net.minecraft.world.item.ItemStack;

public final class RocketItemResolver {

    private RocketItemResolver() {
    }

    public static ItemStack fromRover(ItemStack roverStack) {
        if (!(roverStack.getItem() instanceof IRoversItem roversItem)) {
            return ItemStack.EMPTY;
        }

        return switch (roversItem.getLevel()) {
            case ONE -> new ItemStack(IUItem.rocket.getItem());
            case TWO -> new ItemStack(IUItem.adv_rocket.getItem());
            case THREE -> new ItemStack(IUItem.imp_rocket.getItem());
            case FOUR -> new ItemStack(IUItem.per_rocket.getItem());
        };
    }
}