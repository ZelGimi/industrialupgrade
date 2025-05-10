package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdvSolarEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Collections;
import java.util.List;

public class TileAdvSolarGenerator extends TileSolarGeneratorEnergy {

    public static final double cof = 2;
    private static final List<AABB> aabbs = Collections.singletonList(new AABB(-0.1, 0.0D, 0, 1.1, 1.0D, 1));

    public TileAdvSolarGenerator(BlockPos pos, BlockState state) {

        super(cof, BlockAdvSolarEnergy.adv_se_gen, pos, state);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockAdvSolarEnergy.adv_se_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.adv_se_generator.getBlock();
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }


    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
