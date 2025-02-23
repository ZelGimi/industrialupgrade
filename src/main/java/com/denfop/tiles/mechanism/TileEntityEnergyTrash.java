package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityEnergyTrash extends TileEntityInventory {

    private final Energy energy;
    private final ComponentBaseEnergy expEnergy;
    private final ComponentBaseEnergy quantumEnergy;
    private final ComponentBaseEnergy solariumEnergy;

    public TileEntityEnergyTrash() {
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
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.energy_trash;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

}
