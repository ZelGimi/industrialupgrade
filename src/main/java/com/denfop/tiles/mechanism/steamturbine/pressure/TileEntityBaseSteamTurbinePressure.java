package com.denfop.tiles.mechanism.steamturbine.pressure;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSteamTurbinePressure;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSteamTurbinePressure;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.steamturbine.IPressure;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityBaseSteamTurbinePressure extends TileEntityMultiBlockElement implements IPressure, IUpdatableTileEvent {

    private final int blockLevel;
    private int pressure;

    public TileEntityBaseSteamTurbinePressure(int blockLevel, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);
        this.blockLevel = blockLevel;
        this.pressure = 1;

    }

    @Override
    public int getBlockLevel() {
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
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        pressure = nbtTagCompound.getInt("pressure");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("pressure", pressure);
        return nbtTagCompound;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerSteamTurbinePressure getGuiContainer(final Player var1) {
        return new ContainerSteamTurbinePressure(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSteamTurbinePressure((ContainerSteamTurbinePressure) menu);
    }


    @Override
    public int getPressure() {
        return this.pressure;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            this.pressure = Math.min(this.blockLevel + 2, pressure + 1);
        } else {
            this.pressure = Math.max(1, pressure - 1);
        }
    }


}
