package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.storage.IMonitor;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerFluidMonitor;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenFluidMonitor;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class BlockEntityFluidMonitor extends BlockEntityInventory implements IMonitor {


    private final ComponentBaseEnergy energy;
    public StorageNetwork network;
    public int sizeMode = 0;
    public int prevSizeMode = 0;
    public int decreasing = 0;
    public int fieldMode = 0;
    public String fieldString = "";
    public int viewMode = 0;
    public boolean checkField = true;

    public BlockEntityFluidMonitor(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.fluid_monitor, pos, state);
        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.fluid_monitor;
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
    public ContainerFluidMonitor getGuiContainer(Player var1) {

        return new ContainerFluidMonitor(this, var1);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (checkField) {
            if (this.fieldMode < 2)
                this.fieldString = "";
        }
        this.checkField = true;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer packetBuffer = super.writeContainerPacket();
        packetBuffer.writeInt(sizeMode);
        packetBuffer.writeInt(prevSizeMode);
        packetBuffer.writeInt(viewMode);
        packetBuffer.writeInt(decreasing);
        packetBuffer.writeInt(fieldMode);
        packetBuffer.writeString(fieldString);
        return packetBuffer;
    }

    @Override
    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        sizeMode = customPacketBuffer.readInt();
        prevSizeMode = customPacketBuffer.readInt();
        viewMode = customPacketBuffer.readInt();
        decreasing = customPacketBuffer.readInt();
        fieldMode = customPacketBuffer.readInt();
        fieldString = customPacketBuffer.readString();
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        CompoundTag tag = super.writeToNBT(nbt);
        tag.putInt("sizeMode", sizeMode);
        tag.putInt("prevSizeMode", prevSizeMode);
        tag.putInt("viewMode", viewMode);
        tag.putInt("decreasing", decreasing);
        tag.putInt("fieldMode", fieldMode);
        tag.putString("fieldString", fieldString);
        return tag;
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        sizeMode = nbtTagCompound.getInt("sizeMode");
        prevSizeMode = nbtTagCompound.getInt("prevSizeMode");
        viewMode = nbtTagCompound.getInt("viewMode");
        decreasing = nbtTagCompound.getInt("decreasing");
        fieldMode = nbtTagCompound.getInt("fieldMode");
        fieldString = nbtTagCompound.getString("fieldString");
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenFluidMonitor((ContainerFluidMonitor) isAdmin);
    }

    @Override
    public void setStorageNetwork(StorageNetwork network) {
        this.network = network;
    }
}
