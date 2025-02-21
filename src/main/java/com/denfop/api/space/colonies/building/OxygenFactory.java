package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.Building;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IOxygenFactory;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.api.space.colonies.enums.EnumTypeOxygenFactory;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;

public class OxygenFactory extends Building implements IOxygenFactory {

    private final EnumTypeOxygenFactory type;
    private byte people;

    public OxygenFactory(final IColony colonies, EnumTypeOxygenFactory oxygenFactory, boolean simulate) {
        super(colonies);
        this.type = oxygenFactory;
        this.people = 0;
        if (!simulate) {
            this.getColony().addMaxOxygen(this.getMax());
            this.getColony().addBuilding(this);
        }
    }
    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        super.writePacket(customPacketBuffer);
        customPacketBuffer.writeByte(type.ordinal());
        customPacketBuffer.writeByte(people);
        return customPacketBuffer;
    }
    public OxygenFactory(CustomPacketBuffer packetBuffer, Colony colony) {
        super(colony);
        this.type = EnumTypeOxygenFactory.values()[packetBuffer.readByte()];
        this.people = packetBuffer.readByte();

        this.getColony().addMaxOxygen(this.getMax());
        this.getColony().addBuilding(this);
    }

    @Override
    public int getMinLevelColony() {
        return type.getLevel();
    }

    @Override
    public byte getId() {
        return 5;
    }

    public OxygenFactory(NBTTagCompound tag, final IColony colonie) {
        super(colonie);
        this.type =  EnumTypeOxygenFactory.values()[tag.getByte("id")];
        this.people = tag.getByte("people");
        this.getColony().addMaxOxygen(this.getMax());
        this.getColony().addBuilding(this);
    }

    @Override
    public int getMax() {
        return 5000;
    }

    @Override
    public int getGeneration() {
        return type.getGeneration() * 100;
    }

    @Override
    public int getEnergy() {
        return type.getEnergy();
    }

    @Override
    public int needWorkers() {
        return type.getPeople() - this.people;
    }

    @Override
    public int getPeople() {
        return this.people;
    }

    @Override
    public void addWorkers(final int workers) {
        this.people += (byte) workers;
    }

    @Override
    public void removeWorkers(final int remove) {
        this.people -= remove;
    }

    @Override
    public int getWorkers() {
        return this.people;
    }

    @Override
    public EnumTypeOxygenFactory getType() {
        return type;
    }

    @Override
    public void work() {
        if (this.getColony().getOxygen() < this.getColony().getMaxOxygen()) {
            if (this.getColony().getEnergy() >= type.getEnergy()) {
                this.getColony().useEnergy(type.getEnergy());
                this.getColony().addOxygen(this.getGeneration());
            } else {
                if (!(this.getColony().getProblems().contains(EnumProblems.ENERGY))) {
                    this.getColony().getProblems().add(EnumProblems.ENERGY);
                }
            }
        }
    }

    @Override
    public EnumTypeBuilding getTypeBuilding() {
        return EnumTypeBuilding.OXYGEN;
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setByte("id", (byte) this.type.ordinal());
        tag.setByte("people", this.people);
        return tag;
    }


}
