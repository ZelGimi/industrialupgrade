package com.denfop.blockentity.mechanism.multimechanism.triple;


import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTripleExtractor extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityTripleExtractor(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.TRIPLE_EXTRACTOR.usagePerTick,
                EnumMultiMachine.TRIPLE_EXTRACTOR.lenghtOperation, BlockMoreMachineEntity.triple_extractor, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachineEntity.triple_extractor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_EXTRACTOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtractor2.name");
    }

    public String getStartSoundFile() {
        return "Machines/ExtractorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
