package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntitySolarGeneratorEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;

import java.util.Collections;
import java.util.List;

public class TileEntityImpSolarGenerator extends TileEntitySolarGeneratorEnergy {

    public static final double cof = 4;
    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(-0.2, 0.0D, -0.2, 1.2, 2.0D,
            1.2
    ));

    public TileEntityImpSolarGenerator() {

        super(cof);

    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.ImpblockSE);
    }

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
