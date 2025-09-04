package com.denfop.blockentity.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerRedstoneGenerator extends BlockEntityBaseRedstoneGenerator {

    private final AirPollutionComponent pollutionAir;
    private final SoilPollutionComponent pollutionSoil;

    public BlockEntityPerRedstoneGenerator(BlockPos pos, BlockState state) {
        super(4.6, 8, BlockBaseMachine3Entity.per_redstone_generator, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.per_redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
