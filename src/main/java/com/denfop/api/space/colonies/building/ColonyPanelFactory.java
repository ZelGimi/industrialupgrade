package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.Building;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IColonyPanelFactory;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.api.space.colonies.enums.EnumTypeSolarPanel;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;

public class ColonyPanelFactory extends Building implements IColonyPanelFactory {

    private final EnumTypeSolarPanel type;
    private byte people;

    public ColonyPanelFactory(final IColony colonie, EnumTypeSolarPanel type, boolean simulate) {
        super(colonie);
        this.type = type;
        this.people = 0;
        if (!simulate) {
            this.getColony().addMaxEnergy(5000 * type.getGeneration());
            this.getColony().addBuilding(this);
        }
    }

    public ColonyPanelFactory(final NBTTagCompound tag, final IColony colonie) {
        super(colonie);
        this.type = EnumTypeSolarPanel.getID(tag.getByte("id"));
        this.people = tag.getByte("people");
        this.getColony().addMaxEnergy(5000 * type.getGeneration());
        this.getColony().addBuilding(this);
    }

    public ColonyPanelFactory(CustomPacketBuffer packetBuffer, Colony colonie) {
        super(colonie);
        this.type = EnumTypeSolarPanel.getID(packetBuffer.readByte());
        this.people = packetBuffer.readByte();
        this.getColony().addMaxEnergy(5000 * type.getGeneration());
        this.getColony().addBuilding(this);
    }

    @Override
    public byte getId() {
        return 1;
    }

    @Override
    public int getEnergy() {
        return this.type.getGeneration();
    }

    @Override
    public int getPeople() {
        return people;
    }


    @Override
    public int needWorkers() {
        return this.type.getPeople() - people;
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
    public int getWorkers() {
        return people;
    }

    @Override
    public EnumTypeSolarPanel getType() {
        return this.type;
    }

    @Override
    public void work() {
        if (this.getColony().getEnergy() < this.getColony().getMaxEnergy()) {
            int temp = Math.min(this.getEnergy(), this.getColony().getMaxEnergy() - this.getColony().getEnergy());
            this.getColony().addEnergy(temp);
        }
    }

    @Override
    public EnumTypeBuilding getTypeBuilding() {
        return EnumTypeBuilding.GENERATORS;
    }

    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        super.writePacket(customPacketBuffer);
        customPacketBuffer.writeByte((byte) this.getType().ordinal());
        customPacketBuffer.writeByte(people);
        return customPacketBuffer;
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setByte("people", people);
        tag.setByte("id", (byte) this.getType().ordinal());
        return tag;
    }

    @Override
    public int getMinLevelColony() {
        return type.getLevel();
    }


}
