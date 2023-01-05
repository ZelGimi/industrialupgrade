package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntitySolarGeneratorEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public class TileEntitySolarGenerator extends TileEntitySolarGeneratorEnergy {

    public static final double cof = 1;

    public TileEntitySolarGenerator() {

        super(cof);

    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.blockSE);
    }

}
