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

public class BlockEntityAdvHandlerHeavyOre extends BlockEntityBaseHandlerHeavyOre {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityAdvHandlerHeavyOre(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.ADVANCED, BlockBaseMachine3Entity.double_handlerho, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.35));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.8));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.double_handlerho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
