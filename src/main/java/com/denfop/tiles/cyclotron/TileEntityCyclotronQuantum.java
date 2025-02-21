package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerCyclotronQuantum;
import com.denfop.gui.GuiCyclotronQuantum;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCyclotronQuantum extends TileEntityMultiBlockElement implements IQuantum {


    private final ComponentBaseEnergy positrons;

    public TileEntityCyclotronQuantum() {
        this.positrons = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 10000));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerCyclotronQuantum getGuiContainer(final EntityPlayer var1) {
        return new ContainerCyclotronQuantum(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCyclotronQuantum(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_quantum;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron;
    }


    @Override
    public ComponentBaseEnergy getQuantum() {
        return positrons;
    }

}
