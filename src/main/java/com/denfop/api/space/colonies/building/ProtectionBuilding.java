package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.Building;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IProtectionBuilding;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.colonies.enums.EnumProtectionLevel;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class ProtectionBuilding extends Building implements IProtectionBuilding {

    private final EnumProtectionLevel protection;
    byte people = 0;

    public ProtectionBuilding(EnumProtectionLevel enums, final IColony colonie, boolean simulate) {
        super(colonie);
        this.protection = enums;
        if (!simulate)
            this.getColony().addBuilding(this);
    }

    public ProtectionBuilding(final CompoundTag tag, final IColony colonie) {
        super(colonie);
        this.protection = EnumProtectionLevel.values()[tag.getByte("id")];
        this.getColony().addProtection(this.getProtection());
        this.people = tag.getByte("people");
        this.getColony().addBuilding(this);
    }

    public ProtectionBuilding(CustomPacketBuffer packetBuffer, Colony colonie) {
        super(colonie);
        this.protection = EnumProtectionLevel.values()[packetBuffer.readByte()];
        this.getColony().addProtection(this.getProtection());
        this.people = packetBuffer.readByte();

        this.getColony().addBuilding(this);
    }

    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        super.writePacket(customPacketBuffer);
        customPacketBuffer.writeByte(protection.ordinal());
        customPacketBuffer.writeByte(people);
        return customPacketBuffer;
    }

    public EnumProtectionLevel getProtectionBuilding() {
        return this.protection;
    }


    @Override
    public CompoundTag writeTag(final CompoundTag tag, HolderLookup.Provider p_323640_) {
        super.writeTag(tag, p_323640_);
        tag.putByte("id", (byte) protection.ordinal());
        tag.putByte("people", people);
        return tag;
    }

    @Override
    public int getMinLevelColony() {
        return protection.getLevel();
    }

    @Override
    public byte getId() {
        return 6;
    }

    @Override
    public void work() {
        if (this.getColony().getEnergy() >= this.protection.getEnergy()) {
            this.getColony().useEnergy(this.protection.getEnergy());
            this.getColony().addProtection(this.protection.getProtection());
        } else {
            if (!(this.getColony().getProblems().contains(EnumProblems.ENERGY))) {
                this.getColony().getProblems().add(EnumProblems.ENERGY);
            }
        }
    }

    @Override
    public EnumTypeBuilding getTypeBuilding() {
        return EnumTypeBuilding.PROTECTION;
    }

    @Override
    public int getPeople() {
        return people;
    }

    @Override
    public boolean isIgnore() {
        return true;
    }

    @Override
    public int getProtection() {
        return this.protection.getProtection();
    }

    @Override
    public short needWorkers() {
        return (short) (protection.getPeople() - people);
    }

    @Override
    public void addWorkers(final int addWorkers) {
        people += addWorkers;
    }

    @Override
    public int getWorkers() {
        return people;
    }

    @Override
    public void removeWorkers(final int addWorkers) {
        this.people -= addWorkers;
    }

}
