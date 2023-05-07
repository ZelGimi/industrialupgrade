package com.denfop.componets;

import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ReactorRedstone extends Redstone {

    List<BlockPos> blockPosList = new ArrayList<>();

    public ReactorRedstone(final TileEntityInventory parent) {
        super(parent);
    }

    public void update() {
        try {
            int input = 0;
            for (BlockPos pos : blockPosList) {
                input = this.parent.getWorld().getRedstonePower(pos, EnumFacing.DOWN);

                if (input > 0) {
                    break;
                }
            }
            if (modifiers != null) {
                for (IRedstoneModifier modifier : modifiers) {
                    input = modifier.getRedstoneInput(input);
                }
            }

            if (input != redstoneInput) {
                redstoneInput = input;

                if (changeSubscribers != null) {
                    for (IRedstoneChangeHandler subscriber : changeSubscribers) {
                        subscriber.onRedstoneChange(input);
                    }
                }
            }
        }catch (Exception ignored){};
    }

    public void setBlockPosList(final List<BlockPos> blockPosList) {
        this.blockPosList = blockPosList;
    }

}
