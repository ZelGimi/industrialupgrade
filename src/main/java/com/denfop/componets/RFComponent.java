package com.denfop.componets;

import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.energy.EnergyNet;
import ic2.core.network.GrowingBuffer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.io.DataInput;
import java.io.IOException;

public class RFComponent extends AbstractComponent implements IEnergyReceiver {

    private final AdvEnergy energy;
    public double capacity;
    public double storage;
    private boolean rf;

    public RFComponent(final TileEntityInventory parent, int capacity, AdvEnergy energy) {
        super(parent);
        this.energy = energy;
        this.capacity = capacity;
    }

    public void readFromNbt(NBTTagCompound nbt) {
        this.storage = nbt.getDouble("storage");
        this.rf = nbt.getBoolean("rf");
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storage", this.storage);
        ret.setBoolean("rf", this.rf);
        return ret;
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        GrowingBuffer buffer = new GrowingBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeBoolean(this.rf);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void setStorage(final double storage) {
        this.storage = storage;
    }

    public boolean isRf() {
        return rf;
    }

    public void setRf(final boolean rf) {
        this.rf = rf;
    }

    public void onNetworkUpdate(DataInput is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        this.rf = is.readBoolean();
    }

    public double getCapacity() {
        return this.capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
        this.storage = Math.min(this.capacity, this.storage);
    }

    public double getEnergy() {
        return this.storage;
    }

    public double getFillRatio() {
        return this.storage / this.capacity;
    }

    public double addEnergy(double amount) {
        amount = Math.min(this.capacity - this.storage, amount);
        this.storage += amount;
        this.storage = Math.min(this.storage, this.capacity);
        return amount;
    }

    public boolean canUseEnergy(double amount) {
        return this.storage >= amount;
    }

    public boolean useEnergy(double amount) {
        if (this.storage >= amount) {
            this.storage -= amount;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isServer() {
        return false;
    }

    public double useEnergy(double amount, boolean simulate) {
        double ret = Math.abs(Math.max(0.0D, amount - this.storage) - amount);
        if (!simulate) {
            this.storage -= ret;
        }
        return ret;
    }

    @Override
    public boolean canConnectEnergy(final EnumFacing var1) {
        return true;
    }

    @Override
    public int getEnergyStored(final EnumFacing var1) {
        return (int) this.storage;
    }

    @Override
    public int getMaxEnergyStored(final EnumFacing var1) {
        return (int) this.capacity;
    }

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

        if (this.rf) {
            return receiveEnergy(maxReceive, simulate);
        } else {
            return 0;
        }

    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(
                this.capacity - this.storage,
                Math.min(EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier()) * 4, paramInt)
        );
        if (!paramBoolean) {
            this.storage += i;
        }
        return i;
    }

}
