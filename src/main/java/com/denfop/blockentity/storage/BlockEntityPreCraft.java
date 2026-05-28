package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.storage.PreCraft;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerPreCraft;
import com.denfop.inventory.InventoryPreCraft;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenPreCraft;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityPreCraft extends BlockEntityInventory implements PreCraft, IUpdatableTileEvent {


    public final InventoryPreCraft inputItems;
    private final ComponentBaseEnergy energy;
    public StorageNetwork network;
    List<SameStack> sameStackList;

    public BlockEntityPreCraft(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.precraft, pos, state);
        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));
        this.inputItems = new InventoryPreCraft(this, 10) {
            @Override
            public ItemStack set(int index, ItemStack content) {
                ItemStack stack = super.set(index, content);
                updateCraft();
                return stack;
            }
        };

    }
    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }
    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.precraft;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
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
    public void onLoaded() {
        super.onLoaded();
        updateCraft();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();


    }

    public void updateCraft() {
        if (!(level instanceof ServerLevel))
            return;
        ;
        if (this.network != null) {
            this.network.reBuildPreCraft = true;
            sameStackList = new ArrayList<>();
            for (int i = 0; i < this.inputItems.sameStackList.size(); i++) {
                SameStack sameStack = this.inputItems.sameStackList.get(i);

                if (sameStack.isItem() || sameStack.isFluid()) {

                    SameStack copy = sameStack.copyWithFluid();
                    copy.setAmount(this.inputItems.integerList.get(i));
                    sameStackList.add(copy);
                }
            }
        }
    }

    @Override
    public List<SameStack> getPreCrafts() {
        return sameStackList;
    }

    @Override
    public ContainerPreCraft getGuiContainer(Player var1) {

        return new ContainerPreCraft(this, var1);
    }

    @Override
    public void updateTileServer(Player var1, double var2) {


    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer packetBuffer = super.writeContainerPacket();

        return packetBuffer;
    }

    @Override
    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);

    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        CompoundTag tag = super.writeToNBT(nbt);

        return tag;
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenPreCraft((ContainerPreCraft) isAdmin);
    }

    @Override
    public void setStorageNetwork(StorageNetwork network) {
        this.network = network;
    }
}
