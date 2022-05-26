package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TileImpSolarGenerator extends TileSolarGeneratorEnergy {

    public static final double cof = 4;

    public TileImpSolarGenerator() {

        super(cof, "blockImpSolarGenerator.name");

    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.ImpblockSE);
    }
    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(-0.2, 0.0D, -0.2, 1.2, 2.0D,
            1.2));

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }
}
