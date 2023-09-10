package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;

import java.util.Collections;
import java.util.List;

public class TileImpSolarGenerator extends TileSolarGeneratorEnergy {


    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(-0.2, 0.0D, -0.2, 1.2, 2.0D,
            1.2
    ));

    public TileImpSolarGenerator() {

        super(4);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockImpSolarEnergy.imp_se_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.imp_se_generator;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.imp_se_generator);
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
