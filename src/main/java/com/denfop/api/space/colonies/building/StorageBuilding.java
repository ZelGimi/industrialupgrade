package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.Building;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IColonyStorage;
import com.denfop.api.space.colonies.api.building.IStorage;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.CompoundTag;

public class StorageBuilding extends Building implements IColonyStorage {

    boolean work;
    IStorage storage;
    byte energy;
    byte peoples;

    public StorageBuilding(final IColony colonie, boolean simulate) {
        super(colonie);
        this.storage = new Storage(this);
        this.energy = 10;
        this.peoples = 0;
        this.work = true;
        if (!simulate) {
            this.getColony().addStorage(this.storage);
            this.getColony().addBuilding(this);
        }
    }

    public StorageBuilding(CustomPacketBuffer packetBuffer, Colony colony) {
        super(colony);
        this.peoples = packetBuffer.readByte();
        this.energy = 10;
        this.work = packetBuffer.readBoolean();
        this.storage = new Storage(packetBuffer, this);
        this.getColony().addStorage(this.storage);
        this.getColony().addBuilding(this);
    }

    public StorageBuilding(final CompoundTag tag, final IColony colonie) {
        super(colonie);
        this.peoples = tag.getByte("people");
        this.energy = 10;
        this.work = tag.getBoolean("work");
        this.storage = new Storage(tag.getCompound("storage"), this);
        this.getColony().addStorage(this.storage);
        this.getColony().addBuilding(this);
    }

    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        super.writePacket(customPacketBuffer);
        customPacketBuffer.writeByte(peoples);
        customPacketBuffer.writeBoolean(work);
        storage.writePacket(customPacketBuffer);
        return customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 7;
    }

    @Override
    public CompoundTag writeTag(final CompoundTag tag) {
        super.writeTag(tag);
        tag.putByte("people", this.peoples);
        tag.putBoolean("work", this.work);
        tag.put("storage", this.getStorage().writeNBT(new CompoundTag()));
        return tag;
    }

    @Override
    public int getMinLevelColony() {
        return 10;
    }

    @Override
    public IStorage getStorage() {
        return this.storage;
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }

    @Override
    public int getPeoples() {
        return this.peoples;
    }

    @Override
    public boolean getWork() {
        return this.work;
    }

    @Override
    public void setWork(final boolean setWork) {
        this.work = setWork;
    }

    @Override
    public int getWorkers() {
        return peoples;
    }

    @Override
    public int needWorkers() {
        return 5 - peoples;
    }

    @Override
    public void addWorkers(final int workers) {
        peoples += workers;
    }

    @Override
    public void removeWorkers(final int remove) {
        peoples -= remove;
    }

    @Override
    public void work() {
        if (this.getColony().getEnergy() >= this.getEnergy()) {
            this.getColony().useEnergy(this.getEnergy());
            if (!this.getWork()) {
                this.setWork(true);
            }
        } else {
            if (!this.getColony().getProblems().contains(EnumProblems.ENERGY)) {
                this.getColony().getProblems().add(EnumProblems.ENERGY);
            }
            if (this.getWork()) {
                this.setWork(false);
            }
        }
    }

    @Override
    public EnumTypeBuilding getTypeBuilding() {
        return EnumTypeBuilding.STORAGE;
    }

    @Override
    public int getPeople() {
        return peoples;
    }


}
