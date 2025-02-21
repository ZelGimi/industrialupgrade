package com.denfop.tiles.reactors.graphite.socket;

import com.denfop.componets.Energy;
import com.denfop.container.ContainerGraphiteSocket;
import com.denfop.gui.GuiGraphiteSocket;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.ISocket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMainSocket extends TileEntityMultiBlockElement implements ISocket {

    private final Energy energy;

    public TileEntityMainSocket(int Capacity) {
        this.energy = this.addComponent(Energy.asBasicSource(this, Capacity, 14));
    }

    public Energy getEnergy() {
        return energy;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGraphiteSocket(getGuiContainer(var1));
    }

    @Override
    public ContainerGraphiteSocket getGuiContainer(final EntityPlayer var1) {
        return new ContainerGraphiteSocket(this, var1);
    }

}
