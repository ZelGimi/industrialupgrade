package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Energy;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityEnergyTrash extends TileEntityInventory {

    private final Energy energy;
    private final ComponentBaseEnergy expEnergy;
    private final ComponentBaseEnergy quantumEnergy;
    private final ComponentBaseEnergy solariumEnergy;

    public TileEntityEnergyTrash(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.energy_trash, pos, state);
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
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.energy_trash;
    }


}
