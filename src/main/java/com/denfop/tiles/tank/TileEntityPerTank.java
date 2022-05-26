package com.denfop.tiles.tank;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public class TileEntityPerTank extends TileEntityLiquedTank {

    public TileEntityPerTank() {
        super("BlockPerTank.name", 80, "pertank");
    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.tank, 1, 3);
    }

}
