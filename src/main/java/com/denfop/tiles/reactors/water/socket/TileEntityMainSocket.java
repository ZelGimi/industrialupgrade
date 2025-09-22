package com.denfop.tiles.reactors.water.socket;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerWaterSocket;
import com.denfop.gui.GuiWaterSocket;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ISocket;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class TileEntityMainSocket extends TileEntityMultiBlockElement implements ISocket {

    private final Energy energy;
    private boolean addedToEnergyNet;

    public TileEntityMainSocket(int Capacity) {
        this.energy = this.addComponent((new Energy(
                this,
                Capacity,
                ModUtils.allFacings,
                ModUtils.allFacings,
                14,
                14,
                false
        )));
        this.energy.receivingDisabled = true;
        this.energy.sendingSidabled = false;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

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
        return new GuiWaterSocket(getGuiContainer(var1));
    }

    @Override
    public ContainerWaterSocket getGuiContainer(final EntityPlayer var1) {
        return new ContainerWaterSocket(this, var1);
    }

}
