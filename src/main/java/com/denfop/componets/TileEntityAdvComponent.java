package com.denfop.componets;

import com.denfop.IUCore;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.network.GrowingBuffer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public abstract class TileEntityAdvComponent {

    protected TileEntityInventory parent;

    public TileEntityAdvComponent(final TileEntityInventory parent) {
        this.parent = parent;
    }

    public TileEntityInventory getParent() {
        return this.parent;
    }

    public void setParent(final TileEntityInventory parent) {
        this.parent = parent;
    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return false;
    }

    public void updateEntityServer() {
    }

    public void updateEntityClient() {
    }

    public void onBlockActivated() {
    }

    protected void setNetworkUpdate(EntityPlayerMP player, GrowingBuffer data) {
        IUCore.network.get(true).sendComponentUpdate(this.parent, this.toString(), player, data);
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

    public void readFromNbt(NBTTagCompound nbt) {
    }

    public NBTTagCompound writeToNbt() {
        return null;
    }

    public void onLoaded() {
    }

    public void onUnloaded() {
    }

    public void onNeighborChange(Block srcBlock, BlockPos srcPos) {
    }

    public void onContainerUpdate(EntityPlayerMP player) {
    }

    public void onNetworkUpdate(DataInput is) throws IOException {
    }

    public boolean enableWorldTick() {
        return false;
    }

    public void onWorldTick() {
    }

    public Collection<? extends Capability<?>> getProvidedCapabilities(EnumFacing side) {
        return Collections.emptySet();
    }

    public <T> T getCapability(Capability<T> cap, EnumFacing side) {
        return null;
    }

}
