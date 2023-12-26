package com.denfop.tiles.reactors.heat.socket;

import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerGraphiteSocket;
import com.denfop.container.ContainerHeatSocket;
import com.denfop.gui.GuiGraphiteSocket;
import com.denfop.gui.GuiHeatSocket;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.ISocket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMainSocket extends TileEntityMultiBlockElement implements ISocket {

    private final AdvEnergy energy;

    public TileEntityMainSocket(int Capacity){
        this.energy = this.addComponent( AdvEnergy.asBasicSource(this,Capacity,14));
    }

    public AdvEnergy getEnergy() {
        return energy;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiHeatSocket(getGuiContainer(var1));
    }

    @Override
    public ContainerHeatSocket getGuiContainer(final EntityPlayer var1) {
        return new ContainerHeatSocket(this,var1);
    }

}
