package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCyclotronQuantum;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiCyclotronQuantum;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityCyclotronQuantum extends TileEntityMultiBlockElement implements IQuantum {


    private final ComponentBaseEnergy positrons;

    public TileEntityCyclotronQuantum(BlockPos pos, BlockState state) {
        super( BlockCyclotron.cyclotron_quantum, pos, state);
        this.positrons = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 10000));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerCyclotronQuantum getGuiContainer(final Player var1) {
        return new ContainerCyclotronQuantum(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiCyclotronQuantum((ContainerCyclotronQuantum) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_quantum;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron.getBlock(getTeBlock());
    }


    @Override
    public ComponentBaseEnergy getQuantum() {
        return positrons;
    }

}
