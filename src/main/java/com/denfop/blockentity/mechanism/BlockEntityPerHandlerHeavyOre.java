package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBaseHandlerHeavyOre;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerHandlerHeavyOre extends BlockEntityBaseHandlerHeavyOre {

    private final AirPollutionComponent pollutionAir;
    private final SoilPollutionComponent pollutionSoil;

    public BlockEntityPerHandlerHeavyOre(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.PERFECT, BlockBaseMachine3Entity.quad_handlerho, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.4));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.quad_handlerho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
