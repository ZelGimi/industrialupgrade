package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public class TileSolarGenerator extends TileSolarGeneratorEnergy {


    public TileSolarGenerator() {

        super(1);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolarEnergy.se_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockSE;
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.blockSE);
    }

}
