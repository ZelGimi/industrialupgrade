package com.denfop.api.space.colonies.building;

import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.Building;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.DataItem;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IColonyMiningFactory;
import com.denfop.api.space.colonies.api.building.IStorage;
import com.denfop.api.space.colonies.enums.EnumMiningFactory;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.stream.Collectors;

public class FluidFactory extends Building implements IColonyMiningFactory {

    private final EnumMiningFactory type;
    private byte people;

    public FluidFactory(final IColony colonie, EnumMiningFactory type, boolean simulate) {
        super(colonie);
        this.type = type;
        this.people = 0;
        if (!simulate)
            this.getColony().addBuilding(this);
    }

    public FluidFactory(CustomPacketBuffer packetBuffer, Colony colonie) {
        super(colonie);
        this.type = EnumMiningFactory.getID(packetBuffer.readByte());
        this.people = packetBuffer.readByte();

        this.getColony().addBuilding(this);
    }

    public FluidFactory(final CompoundTag tag, final IColony colonie) {
        super(colonie);
        this.type = EnumMiningFactory.getID(tag.getByte("id"));
        this.people = tag.getByte("people");
        this.getColony().addBuilding(this);
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        super.writePacket(customPacketBuffer);
        customPacketBuffer.writeByte(type.ordinal());
        customPacketBuffer.writeByte(people);
        return customPacketBuffer;
    }

    @Override
    public void work() {
        List<IStorage> storageList = this.getColony().getStorageList();
        if (storageList.isEmpty() || this.getColony().getEnergy() < this.getEnergy()) {
            return;
        }
        if (this.getColony().getTick() % 2 != 0)
            return;
        if (WorldBaseGen.random.nextInt(100) < this.type.getChance()) {
            for (IStorage storage : storageList) {
                if (storage.work()) {
                    List<DataItem<FluidStack>> fluidStacks = SpaceNet.instance.getColonieNet().getFluidsFromBody(getColony().getBody());
                    if (fluidStacks.isEmpty()) {
                        return;
                    }
                    fluidStacks = fluidStacks.stream().filter(fluidStackDataItem -> fluidStackDataItem.getLevel() <= this.getColony().getLevel()).collect(Collectors.toList());
                    if (fluidStacks.isEmpty()) {
                        return;
                    }
                    FluidStack fluidStack = fluidStacks.get(WorldBaseGen.random.nextInt(fluidStacks.size())).getElement();
                    int amount = (int) ((WorldBaseGen.random.nextInt(type.getMaxValue() / 2) + (type.getMaxValue() / 2)) * this.getColony().getPercentEntertainment());
                    fluidStack = new FluidStack(fluidStack.getFluid(), amount);
                    if (storage.canAddFluidStack(fluidStack)) {
                        this.getColony().useEnergy(this.getEnergy());
                        return;
                    }
                }
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
    public CompoundTag writeTag(final CompoundTag tag, HolderLookup.Provider p_323640_) {
        super.writeTag(tag, p_323640_);
        tag.putByte("id", (byte) this.type.ordinal());
        tag.putByte("people", people);
        return tag;
    }

    @Override
    public int getMinLevelColony() {
        return this.type.getLevel();
    }


    @Override
    public int getEnergy() {
        return this.type.getEnergy();
    }


    @Override
    public EnumMiningFactory getFactory() {
        return this.type;
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

}
