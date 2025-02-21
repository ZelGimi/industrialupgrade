package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.Building;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IEntertainment;
import com.denfop.api.space.colonies.enums.EnumEntertainment;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;

public class ColonyEntertainment extends Building implements IEntertainment {

    private final EnumEntertainment type;
    byte people;

    public ColonyEntertainment(EnumEntertainment type, IColony colonies, boolean simulate) {
        super(colonies);
        this.type = type;
        if (!simulate) {
            this.getColony().addBuilding(this);
        }
    }

    @Override
    public boolean isIgnore() {
        return true;
    }

    @Override
    public void work() {
        if (this.people == this.type.getNeedPeople()) {
            if (this.getColony().getEnergy() >= this.type.getEnergy()) {
                this.getColony().useEnergy(this.type.getEnergy());
                this.getColony().addEntertainment(this.type.getEntertainment());
            } else {
                if (!(this.getColony().getProblems().contains(EnumProblems.ENERGY))) {
                    this.getColony().getProblems().add(EnumProblems.ENERGY);
                }
            }
        }

    }

    public ColonyEntertainment(CustomPacketBuffer packetBuffer, Colony colonies) {
        super(colonies);
        int id = packetBuffer.readByte();
        this.type = EnumEntertainment.getID(id);
        this.people = packetBuffer.readByte();
        this.getColony().addBuilding(this);
    }

    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        super.writePacket(customPacketBuffer);
        customPacketBuffer.writeByte(type.ordinal());
        customPacketBuffer.writeByte(people);
        return customPacketBuffer;
    }

    @Override
    public int getMinLevelColony() {
        return type.getLevel();
    }

    public ColonyEntertainment(final NBTTagCompound tag, IColony colonies) {
        super(colonies);
        int id = tag.getByte("id");
        this.type = EnumEntertainment.getID(id);
        this.people = tag.getByte("people");
        this.getColony().addBuilding(this);
    }

    @Override
    public EnumTypeBuilding getTypeBuilding() {
        return EnumTypeBuilding.ENTERTAINMENT;
    }

    @Override
    public byte getId() {
        return 8;
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
    public int needWorkers() {
        return this.type.getNeedPeople() - people;
    }

    @Override
    public void addWorkers(final int workers) {
        people += workers;
    }

    @Override
    public void removeWorkers(final int remove) {
        people -= remove;
    }

    @Override
    public EnumEntertainment getType() {
        return type;
    }

}
