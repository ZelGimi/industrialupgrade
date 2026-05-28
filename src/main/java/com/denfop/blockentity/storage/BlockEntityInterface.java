package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.storage.*;
import com.denfop.api.storage.autocrafting.PatternStack;
import com.denfop.api.storage.autocrafting.TypeRecipe;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerInterface;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenInterface;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlockEntityInterface extends BlockEntityInventory implements IMechanismInterface, IUpdatableTileEvent {

    private final Inventory slotSlots;
    protected ComponentBaseEnergy energy;
    Map<Integer, PatternStack> patternItemHashMap = new HashMap<>();
    List<PatternStack> patternStacks = new LinkedList<>();
    Map<Integer, InterfaceSide> interfaceIndexSideMap = new HashMap<>();
    Map<PatternStack, InterfaceSide> interfaceSideMap = new HashMap<>();
    Map<Direction, BlockEntity> blockEntityMap = new HashMap<>();
    boolean update = false;
    private StorageNetwork network;

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }
    public BlockEntityInterface(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.interface_entity, pos, state);

        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));
        for (int i = 0; i < 4; i++) {
            interfaceIndexSideMap.put(i, InterfaceSide.ANY);
        }
        this.slotSlots = new Inventory(this, Inventory.TypeItemSlot.INPUT, 36) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
                return stack.getItem() instanceof PatternItem && ((PatternItem) stack.getItem()).hasPattern(stack) && ((PatternItem) stack.getItem()).getPattern(stack, registryAccess()).typeRecipe() == TypeRecipe.BLOCK;
            }

            @Override
            public ItemStack set(int i, ItemStack empty) {
                ItemStack stack = super.set(i, empty);
                reload();
                if (network != null) {
                    StorageNetwork storageNetwork = network;
                    storageNetwork.reBuildPatterns = true;
                }
                return stack;
            }
        };

        slotSlots.setStackSizeLimit(1);
    }

    public void reload() {
        patternItemHashMap.clear();
        patternStacks.clear();
        interfaceSideMap.clear();
        for (int i = 0; i < slotSlots.size(); i++) {
            ItemStack stack = slotSlots.getItem(i);
            if (!stack.isEmpty()) {
                PatternItem cellItem = (PatternItem) stack.getItem();
                PatternStack cell = cellItem.getPattern(stack, registryAccess());
                patternItemHashMap.put(i, cell);
                patternStacks.add(cell);
                InterfaceSide side = interfaceIndexSideMap.getOrDefault(i / 9, InterfaceSide.ANY);
                interfaceSideMap.put(cell, side);
            }
        }
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.interface_entity;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }

    public Inventory getSlots() {
        return slotSlots;
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        if (nbt.contains("InterfaceIndexSideMap"))
            interfaceIndexSideMap.clear();


        CompoundTag mapTag = nbt.getCompound("InterfaceIndexSideMap");

        for (String key : mapTag.getAllKeys()) {
            int index = Integer.parseInt(key);
            String enumName = mapTag.getString(key);
            InterfaceSide side = InterfaceSide.valueOf(enumName);
            interfaceIndexSideMap.put(index, side);
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);

        CompoundTag mapTag = new CompoundTag();

        for (Map.Entry<Integer, InterfaceSide> entry : interfaceIndexSideMap.entrySet()) {
            mapTag.putString(
                    String.valueOf(entry.getKey()),
                    entry.getValue().name()
            );
        }

        nbt.put("InterfaceIndexSideMap", mapTag);

        return nbt;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        interfaceIndexSideMap.clear();

        int size = customPacketBuffer.readInt();

        for (int i = 0; i < size; i++) {
            int index = customPacketBuffer.readInt();
            int enumId = customPacketBuffer.readInt();

            InterfaceSide side = InterfaceSide.values()[enumId];
            interfaceIndexSideMap.put(index, side);
        }

    }

    @Override
    public ContainerInterface getGuiContainer(final Player var1) {
        return new ContainerInterface(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player var1, final ContainerMenuBase<?> var2) {
        return new ScreenInterface((ContainerInterface) var2);
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer buffer = super.writeContainerPacket();

        buffer.writeInt(interfaceIndexSideMap.size());

        for (Map.Entry<Integer, InterfaceSide> entry : interfaceIndexSideMap.entrySet()) {
            buffer.writeInt(entry.getKey());
            buffer.writeInt(entry.getValue().ordinal());
        }

        return buffer;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        addStorageSystemConsumptionInformation(tooltip);
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);

    }

    @Override
    public List<ItemStack> getWrenchDrops(final Player player, final int fortune) {
        List<ItemStack> itemStackList = super.getWrenchDrops(player, fortune);


        return itemStackList;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();

        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);

    }

    @Override
    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        this.update = true;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (update) {
            blockEntityMap.clear();
            update = false;
            for (Direction direction : Direction.values()) {
                blockEntityMap.put(direction, this.getLevel().getBlockEntity(pos.offset(direction.getNormal())));
            }
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {

        super.updateField(name, is);
    }


    public void onLoaded() {
        super.onLoaded();
        blockEntityMap.clear();
        for (Direction direction : Direction.values()) {
            blockEntityMap.put(direction, this.getLevel().getBlockEntity(pos.offset(direction.getNormal())));
        }
        for (int i = 0; i < slotSlots.size(); i++) {
            ItemStack stack = slotSlots.getItem(i);
            if (stack.isEmpty()) {
                PatternStack cell = patternItemHashMap.remove(i);
                patternStacks.remove(cell);
            } else {
                PatternItem cellItem = (PatternItem) stack.getItem();
                PatternStack cell = cellItem.getPattern(stack, registryAccess());
                if (!patternItemHashMap.containsKey(i)) {
                    patternItemHashMap.put(i, cell);
                } else {
                    PatternStack cell1 = patternItemHashMap.remove(i);
                    patternItemHashMap.put(i, cell);
                    patternStacks.remove(cell1);
                }
                patternStacks.add(cell);
            }
        }
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
        this.network = network;
        if (this.network != null)
            network.reBuildPatterns = true;
    }


    @Override
    public List<PatternStack> getPatterns() {
        return patternStacks;
    }

    @Override
    public List<ItemStack> getStacks() {
        return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidStacks() {
        return Collections.emptyList();
    }

    public Direction getDirection() {
        return Direction.DOWN;
    }

    public InterfaceSide getSides(PatternStack patternStack) {
        return interfaceSideMap.getOrDefault(patternStack, InterfaceSide.ANY);
    }

    public InterfaceSide getSides(int index) {
        return interfaceIndexSideMap.getOrDefault(index, InterfaceSide.ANY);
    }

    @Override
    public BlockEntity getBlockEntity(Direction direction) {
        return blockEntityMap.get(direction);
    }

    @Override
    public BlockEntity getBlockEntityNeighbor() {
        return blockEntityMap.get(Direction.DOWN);
    }

    @Override
    public EnumTypeSlots getTypeSlots() {
        return EnumTypeSlots.WHITELIST;
    }

    @Override
    public void updateTileServer(Player var1, double var2) {
        int index = (int) (var2 / 10);
        InterfaceSide side = this.interfaceIndexSideMap.getOrDefault(index, InterfaceSide.ANY);
        side = InterfaceSide.values()[(side.ordinal() + 1) % InterfaceSide.values().length];
        this.interfaceIndexSideMap.put(index, side);
        reload();
    }
}
