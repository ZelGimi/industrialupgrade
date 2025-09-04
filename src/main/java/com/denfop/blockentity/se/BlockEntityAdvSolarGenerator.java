package com.denfop.blockentity.se;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntitySolarGeneratorEnergy;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdvSolarEnergyEntity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Collections;
import java.util.List;

public class BlockEntityAdvSolarGenerator extends BlockEntitySolarGeneratorEnergy {

    public static final double cof = 2;
    private static final List<AABB> aabbs = Collections.singletonList(new AABB(-0.1, 0.0D, 0, 1.1, 1.0D, 1));

    public BlockEntityAdvSolarGenerator(BlockPos pos, BlockState state) {

        super(cof, BlockAdvSolarEnergyEntity.adv_se_gen, pos, state);

    }

    public MultiBlockEntity getTeBlock() {
        return BlockAdvSolarEnergyEntity.adv_se_gen;
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
