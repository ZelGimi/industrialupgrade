package com.denfop.blockentity.mechanism.steamturbine.pressure;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.mechanism.steamturbine.IPressure;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamTurbinePressure;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamTurbinePressure;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityBaseSteamTurbinePressure extends BlockEntityMultiBlockElement implements IPressure, IUpdatableTileEvent {

    private final int blockLevel;
    private int pressure;

    public BlockEntityBaseSteamTurbinePressure(int blockLevel, MultiBlockEntity tileBlock, BlockPos pos, BlockState state) {
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
    public ContainerMenuSteamTurbinePressure getGuiContainer(final Player var1) {
        return new ContainerMenuSteamTurbinePressure(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamTurbinePressure((ContainerMenuSteamTurbinePressure) menu);
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
