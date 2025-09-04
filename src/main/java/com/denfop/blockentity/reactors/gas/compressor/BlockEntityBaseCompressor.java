package com.denfop.blockentity.reactors.gas.compressor;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.gas.ICompressor;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCompressor;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenCompressor;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityBaseCompressor extends BlockEntityMultiBlockElement implements ICompressor, IUpdatableTileEvent {

    private final int level;
    private int pressure;

    public BlockEntityBaseCompressor(int level, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
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
    public ContainerMenuCompressor getGuiContainer(final Player var1) {
        return new ContainerMenuCompressor(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenCompressor((ContainerMenuCompressor) menu);
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
