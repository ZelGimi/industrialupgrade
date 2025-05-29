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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ItemFactory extends Building implements IColonyMiningFactory {

    private final EnumMiningFactory type;
    private byte people;

    public ItemFactory(final IColony colonie, EnumMiningFactory type, boolean simulate) {
        super(colonie);
        this.type = type;
        this.people = 0;
        if (!simulate)
            this.getColony().addBuilding(this);
    }

    public ItemFactory(CustomPacketBuffer packetBuffer, Colony colonie) {
        super(colonie);
        this.type = EnumMiningFactory.getID(packetBuffer.readByte());
        this.people = packetBuffer.readByte();
        this.getColony().addBuilding(this);
    }

    public ItemFactory(final CompoundTag tag, final IColony colonie) {
        super(colonie);
        this.type = EnumMiningFactory.getID(tag.getByte("id"));
        this.people = tag.getByte("people");
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
    public byte getId() {
        return 4;
    }

    @Override
    public void work() {
        List<IStorage> storageList = this.getColony().getStorageList();
        if (storageList.isEmpty() || this.getColony().getEnergy() < this.getEnergy()) {
            return;
        }
        if (this.getColony().getTick() % 5 != 0)
            return;
        if (WorldBaseGen.random.nextInt(100) < this.type.getChance()) {
            for (IStorage storage : storageList) {
                if (storage.work()) {
                    List<DataItem<ItemStack>> itemStacks = SpaceNet.instance.getColonieNet().getItemsFromBody(getColony().getBody());
                    if (itemStacks.isEmpty()) {
                        return;
                    }
                    itemStacks = itemStacks.stream().filter(fluidStackDataItem -> fluidStackDataItem.getLevel() <= this.getColony().getLevel()).collect(
                            Collectors.toList());
                    if (itemStacks.isEmpty())
                        return;
                    ItemStack itemStack = itemStacks.get(WorldBaseGen.random.nextInt(itemStacks.size())).getElement().copy();
                    itemStack.setCount((int) ((WorldBaseGen.random.nextInt(this.type.getMaxItemValue()) + 1) * this.getColony().getPercentEntertainment()));
                    if (storage.canAddItemStack(itemStack)) {
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
    public CompoundTag writeTag(final CompoundTag tag) {
        super.writeTag(tag);
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
