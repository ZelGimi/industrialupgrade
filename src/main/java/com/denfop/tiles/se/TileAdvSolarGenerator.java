package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdvSolarEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;

import java.util.Collections;
import java.util.List;

public class TileAdvSolarGenerator extends TileSolarGeneratorEnergy {

    public static final double cof = 2;
    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(-0.1, 0.0D, 0, 1.1, 1.0D, 1));

    public TileAdvSolarGenerator() {

        super(cof);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockAdvSolarEnergy.adv_se_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.adv_se_generator;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.adv_se_generator);
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
