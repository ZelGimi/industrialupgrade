package com.denfop.tiles.mechanism.multimechanism.triple;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileTripleExtractor extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileTripleExtractor(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.TRIPLE_EXTRACTOR.usagePerTick,
                EnumMultiMachine.TRIPLE_EXTRACTOR.lenghtOperation, BlockMoreMachine.triple_extractor, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine.triple_extractor;
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
