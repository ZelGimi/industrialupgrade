package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Collections;
import java.util.List;

public class TileImpSolarGenerator extends TileSolarGeneratorEnergy {


    private static final List<AABB> aabbs = Collections.singletonList(new AABB(-0.2, 0.0D, -0.2, 1.2, 2.0D,
            1.2
    ));

    public TileImpSolarGenerator(BlockPos pos, BlockState state) {

        super(4, BlockImpSolarEnergy.imp_se_gen, pos, state);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockImpSolarEnergy.imp_se_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.imp_se_generator.getBlock();
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }


    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
