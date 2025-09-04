package com.denfop.blockentity.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityRedstoneGenerator extends BlockEntityBaseRedstoneGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityRedstoneGenerator(BlockPos pos, BlockState state) {
        super(1, 1, BlockBaseMachine3Entity.redstone_generator, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.3));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.4));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
