package com.denfop.tiles.reactors.gas.compressor;

import com.denfop.container.ContainerCompressor;
import com.denfop.gui.GuiCompressor;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.ICompressor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBaseCompressor extends TileEntityMultiBlockElement implements ICompressor, IUpdatableTileEvent {

    private final int level;
    private int pressure;

    public TileEntityBaseCompressor(int level) {
        this.level = level;
        this.pressure = 1;

    }

    @Override
    public int getLevel() {
        return this.level;
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
    public ContainerCompressor getGuiContainer(final EntityPlayer var1) {
        return new ContainerCompressor(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCompressor(getGuiContainer(var1));
    }

    @Override
    public int getEnergy() {
        return this.pressure * 5;
    }

    @Override
    public int getPressure() {
        return this.pressure;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0) {
            this.pressure = Math.min(this.level + 1, pressure + 1);
        } else {
            this.pressure = Math.max(1, pressure - 1);
        }
    }

}
