package com.denfop.blockentity.storage;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.storage.Controller;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.api.storage.autocrafting.AutoCraftOutput;
import com.denfop.api.storage.autocrafting.AutoCraftSystem;
import com.denfop.api.storage.autocrafting.Processor;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerBaseProcessor;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenBaseProcessor;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class BlockEntityBaseProcessor extends BlockEntityInventory implements Processor, IUpdatableTileEvent {
    private final int count;
    private final ComponentBaseEnergy energy;
    public int index = -1;
    public AutoCraftSystem autoCraftSystem;
    public boolean hasSystem = false;
    Map<Integer, AutoCraftSystem> autoCraftSystems = new HashMap<>();
    Map<Integer, Map<String, Map<SameStack, Integer>>> itemsForCraft = new HashMap<>();
    List<AutoCraftOutput> craftOutputs = new LinkedList<>();
    int index_processor = -1;
    private BlockPos network;

    public BlockEntityBaseProcessor(MultiBlockEntity block, int count, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.count = count;
        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));

    }
    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }
    public Map<Integer, AutoCraftSystem> getAutoCraftSystems() {
        return autoCraftSystems;
    }

    @Override
    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.hasSystem = customPacketBuffer.readBoolean();

        boolean hasOutput = customPacketBuffer.readBoolean();
        this.index = customPacketBuffer.readInt();

        try {
            this.craftOutputs.clear();
            if (hasOutput) {
                AutoCraftSystem autoCraftSystem = AutoCraftSystem.readFromTag((CompoundTag) DecoderHandler.decode(customPacketBuffer), this.registryAccess());
                this.autoCraftSystem = autoCraftSystem;
            } else {
                autoCraftSystem = null;
            }
            CompoundTag tag = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            ListTag listTag = tag.getList("list", 10);
            for (Tag tag1 : listTag) {
                if (tag1 instanceof CompoundTag compoundTag) {
                    craftOutputs.add(AutoCraftOutput.readFromNBT(compoundTag, this.registryAccess()));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(hasSystem);
        if (index != -1) {
            this.autoCraftSystem = autoCraftSystems.get(index);
            if (autoCraftSystem == null)
                index = -1;
        } else {
            this.autoCraftSystem = null;
        }
        customPacketBuffer.writeBoolean(index != -1);

        customPacketBuffer.writeInt(index);
        ListTag listTag = new ListTag();
        craftOutputs.clear();
        for (AutoCraftSystem autoCraftSystem1 : autoCraftSystems.values()) {
            listTag.add(autoCraftSystem1.getAutoCraftOutput().writeToNBT(this.registryAccess()));
            craftOutputs.add(autoCraftSystem1.getAutoCraftOutput());
        }

        if (index != -1) {
            this.autoCraftSystem = autoCraftSystems.get(index);
            try {
                EncoderHandler.encode(customPacketBuffer, autoCraftSystem.writeToTag(this.registryAccess()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            CompoundTag tag = new CompoundTag();
            tag.put("list", listTag);
            EncoderHandler.encode(customPacketBuffer, tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return customPacketBuffer;
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt = super.writeToNBT(nbt);

        ListTag autoCraftList = new ListTag();
        for (AutoCraftSystem acs : getAutoCrafts()) {
            autoCraftList.add(acs.writeToTag(provider));
        }
        nbt.put("AutoCrafts", autoCraftList);
        saveItemsForCraft(nbt);
        return nbt;

    }

    public CompoundTag saveItemsForCraft(CompoundTag root) {
        CompoundTag outerTag = new CompoundTag();

        for (Map.Entry<Integer, Map<String, Map<SameStack, Integer>>> outerEntry : itemsForCraft.entrySet()) {

            String outerKey = String.valueOf(outerEntry.getKey());
            CompoundTag middleTag = new CompoundTag();

            for (Map.Entry<String, Map<SameStack, Integer>> middleEntry : outerEntry.getValue().entrySet()) {

                String middleKey = middleEntry.getKey();
                ListTag listTag = new ListTag();

                for (Map.Entry<SameStack, Integer> innerEntry : middleEntry.getValue().entrySet()) {

                    CompoundTag elementTag = new CompoundTag();
                    elementTag.put("sameStack", innerEntry.getKey().writeToNBT(this.registryAccess()));
                    elementTag.putInt("count", innerEntry.getValue());

                    listTag.add(elementTag);
                }

                middleTag.put(middleKey, listTag);
            }

            outerTag.put(outerKey, middleTag);
        }

        root.put("itemsForCraft", outerTag);
        return root;
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        ListTag autoCraftList = nbtTagCompound.getList("AutoCrafts", 10);
        for (int i = 0; i < autoCraftList.size(); i++) {
            CompoundTag acTag = autoCraftList.getCompound(i);
            AutoCraftSystem acs = AutoCraftSystem.readFromTag(acTag, provider);
            this.addAutoCraft(acs);
        }
        loadItemsForCraft(nbtTagCompound);
    }

    public void loadItemsForCraft(CompoundTag root) {
        itemsForCraft.clear();

        if (!root.contains("itemsForCraft")) return;

        CompoundTag outerTag = root.getCompound("itemsForCraft");

        for (String outerKey : outerTag.getAllKeys()) {

            int intKey = Integer.parseInt(outerKey);
            CompoundTag middleTag = outerTag.getCompound(outerKey);

            Map<String, Map<SameStack, Integer>> middleMap = new HashMap<>();

            for (String middleKey : middleTag.getAllKeys()) {

                ListTag listTag = middleTag.getList(middleKey, Tag.TAG_COMPOUND);
                Map<SameStack, Integer> innerMap = new HashMap<>();

                for (int i = 0; i < listTag.size(); i++) {

                    CompoundTag elementTag = listTag.getCompound(i);

                    SameStack sameStack =
                            SameStack.readFromNBT(elementTag.getCompound("sameStack"), this.registryAccess());

                    int count = elementTag.getInt("count");

                    innerMap.put(sameStack, count);
                }

                middleMap.put(middleKey, innerMap);
            }

            itemsForCraft.put(intKey, middleMap);
        }
    }

    @Override
    public ContainerBaseProcessor getGuiContainer(Player var1) {

        return new ContainerBaseProcessor(this, var1);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenBaseProcessor((ContainerBaseProcessor) isAdmin);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        addStorageSystemConsumptionInformation(tooltip);
    }


    private void addStorageSystemConsumptionInformation(final List<String> tooltip) {
        tooltip.add("§8" + Localization.translate("iu.storage_system.tooltip.title"));
        tooltip.add("§7" + Localization.translate("iu.storage_system.tooltip.consumption") + ": §b" +
                formatStorageSystemPower(this.getRequiredPower()) + " §7" +
                Localization.translate("iu.storage_system.tooltip.energy_per_tick"));
        tooltip.add("§8" + Localization.translate("iu.storage_system.tooltip.description"));
    }

    private static String formatStorageSystemPower(final double value) {
        if (value == (long) value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    @Override
    public double getRequiredPower() {
        return 1;
    }

    @Override
    public void setStorageNetwork(StorageNetwork network) {
        hasSystem = network != null;
        if (network != null)
            this.network = network.getController().getPosController();
        else
            this.network = null;
    }

    @Override
    public int getMaxSize() {
        return count;
    }

    @Override
    public Collection<AutoCraftSystem> getAutoCrafts() {
        return autoCraftSystems.values();
    }

    @Override
    public AutoCraftSystem getAutoCraft(int id) {
        return autoCraftSystems.get(id);
    }

    @Override
    public Set<Integer> getIndexes() {
        return autoCraftSystems.keySet();
    }

    @Override
    public void cancelAutoCraft(int idAutoCraft) {
        if (hasSystem) {
            AutoCraftSystem system = autoCraftSystems.remove(idAutoCraft);
            Controller controller = (Controller) this.level.getBlockEntity(network);
            itemsForCraft.remove(system.getIndex());
            controller.getStorageNetwork().removeAutoCraft(system);
        }
    }

    public List<AutoCraftOutput> getCraftOutputs() {
        return craftOutputs;
    }

    @Override
    public boolean canAddAutoCraft() {
        return autoCraftSystems.values().size() < count;
    }

    @Override
    public void addAutoCraft(AutoCraftSystem autoCraftSystem) {
        if (autoCraftSystem.getIndex() == -1) {
            List<Integer> indexes = new ArrayList<>(IntStream.range(0, count)
                    .boxed()
                    .toList());
            for (AutoCraftSystem autoCraftSystem1 : autoCraftSystems.values()) {
                indexes.remove((Object) autoCraftSystem1.getIndex());
            }
            autoCraftSystem.setIndex(indexes.get(0));

        }
        autoCraftSystems.put(autoCraftSystem.getIndex(), autoCraftSystem);
        itemsForCraft.put(autoCraftSystem.getIndex(), new HashMap<>());
        autoCraftSystem.getAutoCraftOutput().setIndex(autoCraftSystem.getIndex());
        craftOutputs.add(autoCraftSystem.getAutoCraftOutput());
    }

    @Override
    public int getId() {
        return this.index_processor;
    }

    @Override
    public void setId(int id) {
        this.index_processor = id;
    }

    @Override
    public Map<String, Map<SameStack, Integer>> getItemsForCraft(int idAutoCraft) {
        return itemsForCraft.computeIfAbsent(idAutoCraft, l -> new HashMap<>());
    }

    @Override
    public Map<Integer, Map<String, Map<SameStack, Integer>>> getAllItemsForCraft() {
        return itemsForCraft;
    }

    @Override
    public void removeCraft(Integer b) {
        autoCraftSystems.remove(b);
        craftOutputs.removeIf(autoCraftOutput -> autoCraftOutput.getIndex() == b);
    }

    @Override
    public void removeNetworkCraft(Integer b) {
        craftOutputs.removeIf(autoCraftOutput -> autoCraftOutput.getIndex() == b);
        this.itemsForCraft.remove(b);
    }

    @Override
    public void updateTileServer(Player var1, double var2) {
        if (var2 >= 0)
            this.index = (int) var2;
        else {
            cancelAutoCraft((int) Math.abs(var2) - 1);
        }
    }
}
