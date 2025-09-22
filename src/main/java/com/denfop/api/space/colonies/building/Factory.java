package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.Building;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IFactory;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.api.space.colonies.enums.EnumTypeFactory;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;

public class Factory extends Building implements IFactory {

    public final EnumTypeFactory typeFactory;
    byte people;

    public Factory(final IColony colonie, EnumTypeFactory typeFactory, boolean simulate) {
        super(colonie);
        this.typeFactory = typeFactory;
        if (!simulate) {
            this.getColony().addBuilding(this);
        }
    }

    public Factory(CustomPacketBuffer packetBuffer, Colony colonie) {
        super(colonie);
        int id = packetBuffer.readByte();
        this.typeFactory = EnumTypeFactory.getID(id);
        this.people = packetBuffer.readByte();
        this.getColony().addBuilding(this);
    }

    public Factory(final NBTTagCompound tag, IColony colonie) {
        super(colonie);
        int id = tag.getByte("id");
        this.typeFactory = EnumTypeFactory.getID(id);
        this.people = tag.getByte("people");
        this.getColony().addBuilding(this);
    }

    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        super.writePacket(customPacketBuffer);
        customPacketBuffer.writeByte(typeFactory.ordinal());
        customPacketBuffer.writeByte(people);
        return customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 2;
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setByte("id", (byte) this.getType().ordinal());
        tag.setByte("people", people);
        return tag;
    }

    @Override
    public int getMinLevelColony() {
        return this.getType().getLevel();
    }

    @Override
    public void work() {
        if (this.getColony().getEnergy() >= this.typeFactory.getEnergy()) {
            this.getColony().useEnergy(this.typeFactory.getEnergy());
            this.getColony().addFood((int) (this.people * 2 * this.getColony().getPercentEntertainment()));
        } else {
            if (!(this.getColony().getProblems().contains(EnumProblems.ENERGY))) {
                this.getColony().getProblems().add(EnumProblems.ENERGY);
            }
        }
    }

    @Override
    public EnumTypeBuilding getTypeBuilding() {
        return EnumTypeBuilding.FABRIC;
    }

    @Override
    public int getPeople() {
        return people;
    }


    @Override
    public int getWorkers() {
        return people;
    }

    @Override
    public int getEnergy() {
        return this.getType().getEnergy();
    }

    @Override
    public EnumTypeFactory getType() {
        return this.typeFactory;
    }

    @Override
    public int needWorkers() {
        return this.typeFactory.getPeople() - people;
    }

    @Override
    public void addWorkers(final int workers) {
        people += workers;
    }

    @Override
    public void removeWorkers(final int remove) {
        people -= remove;
    }

}
