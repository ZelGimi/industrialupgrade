package com.denfop.tiles.reactors.gas.compressor;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCompressor;
import com.denfop.gui.GuiCompressor;
import com.denfop.gui.GuiCore;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.ICompressor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityBaseCompressor extends TileEntityMultiBlockElement implements ICompressor, IUpdatableTileEvent {

    private final int level;
    private int pressure;

    public TileEntityBaseCompressor(int level, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block,pos,state);
        this.level = level;
        this.pressure = 1;

    }

    @Override
    public int getBlockLevel() {
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
    public ContainerCompressor getGuiContainer(final Player var1) {
        return new ContainerCompressor(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiCompressor((ContainerCompressor) menu);
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
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            this.pressure = Math.min(this.level + 1, pressure + 1);
        } else {
            this.pressure = Math.max(1, pressure - 1);
        }
    }

}
