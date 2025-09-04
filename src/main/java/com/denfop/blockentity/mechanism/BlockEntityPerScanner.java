package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityScanner;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerScanner extends BlockEntityScanner {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityPerScanner(BlockPos pos, BlockState state) {
        super(1500, BlockBaseMachine3Entity.per_scanner, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.025));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.per_scanner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
