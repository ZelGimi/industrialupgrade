package com.denfop.blockentity.mechanism.multimechanism.simple;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class BlockEntityRecycler extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityRecycler(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.RECYCLER.usagePerTick,
                EnumMultiMachine.RECYCLER.lenghtOperation, BlockSimpleMachineEntity.recycler_iu, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("recycler_soil_pollution_amount", 0.1D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("recycler_air_pollution_amount", 0.15D)));
    }

    public void initiate(int soundEvent) {
        if (this.getWorld().getGameTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
    }

    public MultiBlockEntity getTeBlock() {
        return BlockSimpleMachineEntity.recycler_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.simplemachine.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.RECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRecycler.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
