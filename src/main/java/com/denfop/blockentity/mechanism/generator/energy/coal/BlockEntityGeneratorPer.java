package com.denfop.blockentity.mechanism.generator.energy.coal;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGeneratorPer extends BlockEntityAdvGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityGeneratorPer(BlockPos pos, BlockState state) {
        super(4.6, 32000, 4, BlockBaseMachineEntity.per_gen, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.per_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }

}
