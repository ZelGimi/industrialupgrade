package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Energy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEnergyTrash extends BlockEntityInventory {

    private final Energy energy;
    private final ComponentBaseEnergy expEnergy;
    private final ComponentBaseEnergy quantumEnergy;
    private final ComponentBaseEnergy solariumEnergy;

    public BlockEntityEnergyTrash(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.energy_trash, pos, state);
        energy = this.addComponent(Energy.asBasicSink(this, 100000000000000000D, 14));
        expEnergy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.EXPERIENCE, this, 100000000000000000D));
        quantumEnergy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 100000000000000000D));
        solariumEnergy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.SOLARIUM, this, 100000000000000000D));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.energy.useEnergy(this.energy.getEnergy());
        this.expEnergy.useEnergy(this.expEnergy.getEnergy());
        this.quantumEnergy.useEnergy(this.quantumEnergy.getEnergy());
        this.solariumEnergy.useEnergy(this.solariumEnergy.getEnergy());
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.energy_trash;
    }


}
