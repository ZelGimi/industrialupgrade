package com.denfop.api.energy;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public abstract class BasicEnergyTile implements IAdvEnergyTile {

    protected World world;
    protected TileEntity tile;
    protected BlockPos pos;
    protected double capacity;
    protected double energyStored;
    protected boolean addedToEnet;


    public BasicEnergyTile(TileEntity locationProvider, double capacity) {
        this.world = locationProvider.getWorld();
        this.pos = locationProvider.getPos();
        this.capacity = capacity;
    }


    public void update() {
        if (!this.addedToEnet) {
            this.onLoad();
        }

    }

    public void onLoad() {
        if (!this.addedToEnet && !this.getWorldObj().isRemote && Info.isIc2Available()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnet = true;
        }

    }

    public void invalidate() {
        this.onChunkUnload();
    }

    public void onChunkUnload() {
        if (this.addedToEnet && !this.getWorldObj().isRemote && Info.isIc2Available()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnet = false;
        }

    }

    public void readFromNBT(NBTTagCompound tag) {
        NBTTagCompound data = tag.getCompoundTag(this.getNbtTagName());
        this.setEnergyStored(data.getDouble("energy"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound data = new NBTTagCompound();
        data.setDouble("energy", this.getEnergyStored());
        tag.setTag(this.getNbtTagName(), data);
        return tag;
    }

    public double getCapacity() {
        return this.capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getEnergyStored() {
        return this.energyStored;
    }

    public void setEnergyStored(double amount) {
        this.energyStored = amount;
    }

    public double getFreeCapacity() {
        return this.getCapacity() - this.getEnergyStored();
    }

    public double addEnergy(double amount) {
        if (this.getWorldObj().isRemote) {
            return 0.0D;
        } else {
            double energyStored = this.getEnergyStored();
            double capacity = this.getCapacity();
            if (amount > capacity - energyStored) {
                amount = capacity - energyStored;
            }

            this.setEnergyStored(energyStored + amount);
            return amount;
        }
    }

    public boolean canUseEnergy(double amount) {
        return this.getEnergyStored() >= amount;
    }

    public boolean useEnergy(double amount) {
        if (this.canUseEnergy(amount) && !this.getWorldObj().isRemote) {
            this.setEnergyStored(this.getEnergyStored() - amount);
            return true;
        } else {
            return false;
        }
    }

    public boolean charge(ItemStack stack) {
        if (stack != null && Info.isIc2Available() && !this.getWorldObj().isRemote) {
            double energyStored = this.getEnergyStored();
            double amount = ElectricItem.manager.charge(
                    stack,
                    energyStored,
                    Math.max(this.getSinkTier(), this.getSourceTier()),
                    false,
                    false
            );
            this.setEnergyStored(energyStored - amount);
            return amount > 0.0D;
        } else {
            return false;
        }
    }

    public boolean discharge(ItemStack stack, double limit) {
        if (stack != null && Info.isIc2Available() && !this.getWorldObj().isRemote) {
            double energyStored = this.getEnergyStored();
            double amount = this.getCapacity() - energyStored;
            if (amount <= 0.0D) {
                return false;
            } else {
                if (limit > 0.0D && limit < amount) {
                    amount = limit;
                }

                amount = ElectricItem.manager.discharge(
                        stack,
                        amount,
                        Math.max(this.getSinkTier(), this.getSourceTier()),
                        limit > 0.0D,
                        true,
                        false
                );
                this.setEnergyStored(energyStored + amount);
                return amount > 0.0D;
            }
        } else {
            return false;
        }
    }

    public World getWorldObj() {


        return this.world;
    }

    public BlockPos getPosition() {

        return this.pos;
    }


    protected abstract String getNbtTagName();

    protected int getSinkTier() {
        return 0;
    }

    protected int getSourceTier() {
        return 0;
    }

}
