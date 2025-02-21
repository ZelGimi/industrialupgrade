package com.denfop.tiles.mechanism.steamturbine.pressure;

import com.denfop.container.ContainerCompressor;
import com.denfop.container.ContainerSteamTurbinePressure;
import com.denfop.gui.GuiCompressor;
import com.denfop.gui.GuiSteamTurbinePressure;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.steamturbine.IPressure;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBaseSteamTurbinePressure extends TileEntityMultiBlockElement implements IPressure, IUpdatableTileEvent {

    private final int level;
    private int pressure;

    public TileEntityBaseSteamTurbinePressure(int level) {
        this.level = level;
        this.pressure = 1;

    }

    @Override
    public int getLevel() {
        return -1;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        pressure = customPacketBuffer.readInt();
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(pressure);
        return customPacketBuffer;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        pressure = nbtTagCompound.getInteger("pressure");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("pressure", pressure);
        return nbtTagCompound;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerSteamTurbinePressure getGuiContainer(final EntityPlayer var1) {
        return new ContainerSteamTurbinePressure(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSteamTurbinePressure(getGuiContainer(var1));
    }


    @Override
    public int getPressure() {
        return this.pressure;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0) {
            this.pressure = Math.min(this.level + 2, pressure + 1);
        } else {
            this.pressure = Math.max(1, pressure - 1);
        }
    }


}
