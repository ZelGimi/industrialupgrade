package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.*;
import com.denfop.api.storage.Controller;
import com.denfop.api.storage.ElectricStorage;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuTank;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCreateNetwork;
import com.denfop.network.packet.PacketUpdateNetworkSystem;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockEntityController extends BlockEntityInventory implements Controller {


    public final Energy energyDefault;
    public final StorageNetwork networkSystem;
    public ComponentBaseEnergy energy;
    private List<Path> paths;

    public BlockEntityController(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.controller, pos, state);

        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSource(EnergyType.STORAGE, this, 1));
        this.energyDefault = this.addComponent(Energy.asBasicSink(this, 500000, 14));
        this.networkSystem = new StorageNetwork(this);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.controller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }
    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }
    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        if (!this.getLevel().isClientSide) {
            paths = EnergyBase.storage.getLocalSystem(this.getLevel()).getPaths((ISource) this.energy.delegate);
            List<BlockPos> posList = new ArrayList<>();
            if (paths != null)
                paths.forEach(path -> {
                    ISink sink = path.getSink();
                    BlockEntity blockEntity = this.getLevel().getBlockEntity(sink.getPos());
                    if (blockEntity instanceof ElectricStorage storage) {
                        networkSystem.addElement(storage);
                        posList.add(sink.getPos());
                    }
                });
            if (!posList.isEmpty()) {
                new PacketCreateNetwork(pos, level, posList);
                new PacketUpdateNetworkSystem(level, posList, pos, false);
            }
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
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

    }

    @Override
    public void updateEntityClient() {
        super.updateEntityClient();
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        List<Path> pathTemp = paths;
        paths = EnergyBase.storage.getLocalSystem(this.level).getPaths((ISource) this.energy.delegate);
        this.energy.buffer.storage = 1;
        if (paths == null && pathTemp != null || paths != null && pathTemp == null || paths != null && paths.size() != pathTemp.size()) {
            networkSystem.reBuild(paths, level);
            List<BlockPos> posList = new ArrayList<>();
            if (paths != null)
                paths.forEach(path -> {
                    ISink sink = path.getSink();
                    BlockEntity blockEntity = this.getLevel().getBlockEntity(sink.getPos());
                    if (blockEntity instanceof ElectricStorage) {
                        posList.add(sink.getPos());
                    }
                });

            new PacketCreateNetwork(pos, level, new ArrayList<>(posList));
            new PacketUpdateNetworkSystem(level, new ArrayList<>(posList), pos, false);
        }
        this.networkSystem.tick(level, energyDefault, pos);
    }

    public void updateField(String name, CustomPacketBuffer is) {

        super.updateField(name, is);
    }


    public ContainerMenuTank getGuiContainer(Player entityPlayer) {
        return null;
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return null;
    }


    public void onLoaded() {
        super.onLoaded();
        if (!this.level.isClientSide) {

        }
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        this.networkSystem.onUnload(this.level);
    }


    private void addStorageSystemConsumptionInformation(final List<String> tooltip) {
        tooltip.add(ChatFormatting.GOLD + Localization.translate("iu.storage_system.tooltip.header"));
        tooltip.add(ChatFormatting.GRAY + Localization.translate("iu.storage_system.tooltip.consumption_system")
                + ": " + ChatFormatting.YELLOW + formatStorageSystemPower(this.networkSystem.getConsumeEnergy()) + " "
                + Localization.translate("iu.storage_system.tooltip.unit"));
        tooltip.add(ChatFormatting.DARK_GRAY + Localization.translate("iu.storage_system.tooltip.consumption.description"));
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
    public double getPower() {
        return this.energyDefault.getEnergy();
    }

    @Override
    public BlockPos getPosController() {
        return pos;
    }

    @Override
    public void setStackForCraft(Map<String, Map<SameStack, Integer>> stacksMapCount) {
        this.networkSystem.stacksMapCount = stacksMapCount;
    }

    @Override
    public void setStack(boolean isFluid, boolean isCreate, List<SameStack> stacks) {
        if (!isCreate) {
            if (isFluid) {
                this.networkSystem.setFluids(stacks);
            } else {
                this.networkSystem.setItems(stacks);
            }
        } else {
            if (isFluid) {
                this.networkSystem.setFluidsCreate(stacks);
            } else {
                this.networkSystem.setItemsCreate(stacks);
            }
        }
    }

    @Override
    public StorageNetwork getStorageNetwork() {
        return networkSystem;
    }

    @Override
    public void setStorageNetwork(StorageNetwork network) {

    }
}
