package com.denfop.api.space.colonies;

import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.api.IBuildingItem;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.IColonyBuilding;
import com.denfop.api.space.colonies.api.IColonyNet;
import com.denfop.api.space.colonies.api.building.IStorage;
import com.denfop.api.space.colonies.building.ColonyHouse;
import com.denfop.api.space.colonies.building.ColonyPanelFactory;
import com.denfop.api.space.colonies.building.Factory;
import com.denfop.api.space.colonies.building.OxygenFactory;
import com.denfop.api.space.colonies.building.ProtectionBuilding;
import com.denfop.api.space.colonies.enums.EnumHousesLevel;
import com.denfop.api.space.colonies.enums.EnumProtectionLevel;
import com.denfop.api.space.colonies.enums.EnumTypeFactory;
import com.denfop.api.space.colonies.enums.EnumTypeSolarPanel;
import com.denfop.api.space.fakebody.Data;
import com.denfop.network.packet.PacketSuccessUpdateColony;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ColonyNet implements IColonyNet {

    Map<UUID, List<IColony>> fakePlayerListMap;
    List<IColony> colonyList;
    List<UUID> fakePlayerList;
    Map<IBody, List<DataItem<ItemStack>>> bodyItemStackList = new HashMap<>();
    Map<IBody, List<DataItem<FluidStack>>> bodyFluidStackList = new HashMap<>();
    Map<UUID, List<Sends>> sends = new HashMap<>();
    List<IColony> deleteList = new LinkedList<>();

    public ColonyNet() {
        this.fakePlayerListMap = new HashMap<>();
        this.colonyList = new LinkedList<>();
        this.fakePlayerList = new LinkedList<>();
    }

    @Override
    public Map<UUID, List<IColony>> getMap() {
        return this.fakePlayerListMap;
    }

    @Override
    public boolean canAddColony(final IBody body, final EntityPlayer player) {
        final Data data = SpaceNet.instance
                .getFakeSpaceSystem()
                .getDataFromUUID(player.getUniqueID())
                .get(body);
        if (data == null) {
            return false;
        }
        final IColonyBuilding houses = hasItemInInventory(player, ColonyHouse.class, 3);
        if (houses == null) {
            return false;
        }
        if (((ColonyHouse) houses).getLevel() != EnumHousesLevel.LOW) {
            return false;
        }
        final IColonyBuilding oxygen = hasItemInInventory(player, OxygenFactory.class, 1);
        if (oxygen == null) {
            return false;
        }
        final IColonyBuilding factory = hasItemInInventory(player, Factory.class, 2);
        if (factory == null) {
            return false;
        }
        if (((Factory) factory).getType() != EnumTypeFactory.LOW) {
            return false;
        }
        final IColonyBuilding protection = hasItemInInventory(player, ProtectionBuilding.class, 1);
        if (protection == null) {
            return false;
        }
        if (((ProtectionBuilding) protection).getProtectionBuilding() != EnumProtectionLevel.LOW) {
            return false;
        }
        final IColonyBuilding panel = hasItemInInventory(player, ColonyPanelFactory.class, 1);
        if (panel == null) {
            return false;
        }
        if (((ColonyPanelFactory) panel).getType() != EnumTypeSolarPanel.LOW) {
            return false;
        }
        List<IColony> list = fakePlayerListMap.get(player.getUniqueID());
        if (list == null) {
            return true;
        }
        for (IColony colony : list) {
            if (colony.matched(body)) {
                return false;
            }
        }
        return true;
    }

    private IColonyBuilding hasItemInInventory(EntityPlayer player, Class<?> colonyClass, int count) {
        final NonNullList<ItemStack> mainInventory = player.inventory.mainInventory;
        for (ItemStack stack : mainInventory) {
            if (stack.getItem() instanceof IBuildingItem && stack.getCount() >= count) {
                IBuildingItem buildingItem = (IBuildingItem) stack.getItem();
                final IColonyBuilding building = buildingItem.getBuilding(null, stack, true);
                if (colonyClass.isInstance(building)) {
                    return building;
                }
            }
        }
        return null;
    }

    private void consumeItemInInventory(EntityPlayer player, Class<?> colonyClass, int count, Colony colony) {
        final NonNullList<ItemStack> mainInventory = player.inventory.mainInventory;
        for (ItemStack stack : mainInventory) {
            if (stack.getItem() instanceof IBuildingItem && stack.getCount() >= count) {
                IBuildingItem buildingItem = (IBuildingItem) stack.getItem();
                final IColonyBuilding building = buildingItem.getBuilding(colony, stack, true);
                if (colonyClass.isInstance(building)) {
                    for (int i = 0; i < count; i++) {
                        buildingItem.getBuilding(colony, stack, false);
                    }
                    stack.shrink(count);
                }
            }
        }
    }

    public void addItemToColony(IBody body, EntityPlayer player) {
        List<IColony> list = fakePlayerListMap.get(player.getUniqueID());
        if (list == null) {
            return;
        }
        for (IColony colony : list) {
            if (colony.matched(body)) {
                ItemStack stack = player.inventory.getItemStack();
                if (stack.getItem() instanceof IBuildingItem) {
                    IBuildingItem buildingItem = (IBuildingItem) stack.getItem();
                    IColonyBuilding building = buildingItem.getBuilding(colony, stack, true);
                    if (colony.getLevel() >= building.getMinLevelColony() && colony.getAvailableBuilding() > 0) {
                        buildingItem.getBuilding(colony, stack, false);
                        stack.shrink(1);
                        new PacketSuccessUpdateColony(player);
                        break;
                    }
                }

                break;
            }
        }
    }

    @Override
    public void addColony(final IBody body, final EntityPlayer player) {
        if (canAddColony(body, player)) {
            List<IColony> colonyList;
            final Colony colony = new Colony(body, player.getUniqueID());
            consumeItemInInventory(player, ColonyHouse.class, 3, colony);
            consumeItemInInventory(player, Factory.class, 2, colony);
            consumeItemInInventory(player, OxygenFactory.class, 1, colony);
            consumeItemInInventory(player, ProtectionBuilding.class, 1, colony);
            consumeItemInInventory(player, ColonyPanelFactory.class, 1, colony);
            colonyList = this.fakePlayerListMap.computeIfAbsent(player.getUniqueID(), l -> new LinkedList<>());
            if (colonyList.isEmpty()) {
                this.fakePlayerList.add(player.getUniqueID());
            }
            colonyList.add(colony);
            this.colonyList.add(colony);
        }
    }

    @Override
    public void removeColony(final IColony colony, final UUID player) {
        List<IColony> colonyList = fakePlayerListMap.get(player);
        colonyList.remove(colony);
        this.colonyList.remove(colony);
    }

    @Override
    public void removeColony(final IBody body, final UUID player) {
        List<IColony> colonyList = fakePlayerListMap.get(player);
        IColony colony = null;
        for (IColony colony1 : colonyList) {
            if (colony1.getBody() == body) {
                colony = colony1;
            }
        }
        colonyList.remove(colony);
        this.colonyList.remove(colony);
    }

    public List<Sends> getSendsFromUUID(UUID uuid) {
        return sends.getOrDefault(uuid, Collections.emptyList());
    }

    @Override
    public void working() {
        for (Map.Entry<UUID, List<Sends>> uuidListEntry : this.sends.entrySet()) {
            final Iterator<Sends> iter = uuidListEntry.getValue().iterator();
            while (iter.hasNext()) {
                Sends sends = iter.next();
                sends.works();
                if (sends.needRemove()) {
                    iter.remove();
                }
            }
        }
        for (IColony colony : colonyList) {
            colony.update();
            if (colony.getTimeToDelete() <= 0) {
                deleteList.add(colony);
            }
        }
        for (IColony colony : deleteList) {
            removeColony(colony, colony.getFakePlayer());
        }
        deleteList.clear();
    }

    @Override
    public List<IColony> getColonies() {
        return this.colonyList;
    }

    public void addFluidStack(IBody body, short level, FluidStack fluidStack) {
        List<DataItem<FluidStack>> list = bodyFluidStackList.computeIfAbsent(body, k -> new ArrayList<>());
        list.add(new DataItem<>(level, fluidStack));
    }

    public void addItemStack(IBody body, short level, ItemStack fluidStack) {
        List<DataItem<ItemStack>> list = bodyItemStackList.computeIfAbsent(body, k -> new ArrayList<>());
        list.add(new DataItem<>(level, fluidStack));
    }

    @Override
    public List<DataItem<FluidStack>> getFluidsFromBody(final IBody body) {
        return bodyFluidStackList.getOrDefault(body, Collections.emptyList());
    }

    @Override
    public List<DataItem<ItemStack>> getItemsFromBody(final IBody body) {
        return bodyItemStackList.getOrDefault(body, Collections.emptyList());
    }

    @Override
    public NBTTagCompound writeNBT(final NBTTagCompound tag, UUID player) {
        final List<IColony> list = fakePlayerListMap.getOrDefault(player, Collections.emptyList());
        NBTTagList nbt = new NBTTagList();
        tag.setUniqueId("player", player);
        for (IColony colonies : list) {
            nbt.appendTag(colonies.writeNBT(new NBTTagCompound()));
        }
        NBTTagList nbt1 = new NBTTagList();
        for (Sends sends1 : sends.getOrDefault(player, Collections.emptyList())) {
            nbt1.appendTag(sends1.writeToNbt());
        }
        tag.setTag("sends", nbt1);
        tag.setTag("colonial", nbt);
        return tag;
    }

    @Override
    public void addColony(final NBTTagCompound tag) {
        NBTTagList nbt = tag.getTagList("colonial", 10);
        NBTTagList nbt1 = tag.getTagList("sends", 10);
        List<IColony> list;

        UUID player = tag.getUniqueId("player");
        if (this.fakePlayerList.contains(player)) {
            return;
        }
        this.fakePlayerList.add(player);
        list = new LinkedList<>();
        List<Sends> list1 = new LinkedList<>();
        for (int i = 0; i < nbt1.tagCount(); i++) {
            NBTTagCompound nbt2 = nbt1.getCompoundTagAt(i);
            Sends sends1 = new Sends(nbt2);
            list1.add(sends1);
        }

        for (int i = 0; i < nbt.tagCount(); i++) {
            NBTTagCompound nbt2 = nbt.getCompoundTagAt(i);
            IColony colonies = new Colony(nbt2, player);
            list.add(colonies);
            colonyList.add(colonies);

        }
        sends.put(player, list1);
        fakePlayerListMap.put(player, new ArrayList<>(list));
    }

    @Override
    public List<UUID> getList() {
        return this.fakePlayerList;
    }

    @Override
    public void unload() {
        this.fakePlayerListMap.clear();
        this.colonyList.clear();
        this.fakePlayerList.clear();
        this.sends.clear();
    }

    @Override
    public void sendResourceToPlanet(final UUID uniqueID, final IBody body1) {
        final List<IColony> colonies = this.fakePlayerListMap.get(uniqueID);
        if (colonies != null) {
            for (IColony colony : colonies) {
                if (colony.matched(body1)) {
                    Sends sends1 = new Sends(uniqueID, body1, colony);
                    List<IStorage> storages = colony.getStorageList();
                    for (IStorage storage : storages) {
                        for (ItemStack stack : storage.getStacks()) {
                            if (sends1.stacks.size() == 27) {
                                break;
                            }
                            if (stack != null && !stack.isEmpty())
                            sends1.addStack(stack.copy());
                        }
                        for (FluidStack fluidStack : storage.getFluidStacks()) {
                            if (sends1.fluidStacks.size() == 9) {
                                break;
                            }
                            if (fluidStack != null) {
                                sends1.addStack(fluidStack.copy());
                            }
                        }
                        storage.getStacks().clear();
                        storage.getFluidStacks().clear();
                    }
                    if (!sends1.stacks.isEmpty() || !sends1.fluidStacks.isEmpty()) {
                        final List<Sends> list = this.sends.computeIfAbsent(uniqueID, k -> new LinkedList<>());
                        list.add(sends1);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void setAutoSends(final UUID uniqueID, final IBody body1) {
        final List<IColony> colonies = this.fakePlayerListMap.get(uniqueID);
        if (colonies != null) {
            for (IColony colony : colonies) {
                if (colony.matched(body1)) {
                    colony.setAuto(!colony.isAuto());
                    break;
                }
            }
        }
    }

}
