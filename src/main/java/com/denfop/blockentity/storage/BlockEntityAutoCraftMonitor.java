package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.storage.IMonitor;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.api.storage.autocrafting.AutoCraftOutput;
import com.denfop.api.storage.autocrafting.AutoCraftSystem;
import com.denfop.api.storage.autocrafting.Processor;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerAutoCraftMonitor;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenAutoCraftMonitor;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

public class BlockEntityAutoCraftMonitor extends BlockEntityInventory implements IMonitor, IUpdatableTileEvent {

    private final ComponentBaseEnergy energy;
    public int processorId = -1;
    public int index = -1;
    public AutoCraftSystem autoCraftSystem;
    public boolean hasSystem = false;
    Map<Integer, List<AutoCraftOutput>> craftOutputs = new HashMap<>();
    private StorageNetwork network;
    public BlockEntityAutoCraftMonitor(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.autocraft_monitor, pos, state);
        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));

    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.autocraft_monitor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (ForgeCapabilities.ITEM_HANDLER == cap)
            return LazyOptional.empty();
        return super.getCapability(cap, facing);
    }
    @Override
    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.hasSystem = customPacketBuffer.readBoolean();

        if (hasSystem) {
            try {
                boolean hasOutput = customPacketBuffer.readBoolean();
                this.index = customPacketBuffer.readInt();
                this.processorId = customPacketBuffer.readInt();

                this.craftOutputs.clear();

                if (hasOutput) {
                    AutoCraftSystem autoCraftSystem = AutoCraftSystem.readFromTag(
                            (CompoundTag) DecoderHandler.decode(customPacketBuffer)
                    );
                    this.autoCraftSystem = autoCraftSystem;
                } else {
                    this.autoCraftSystem = null;
                }

                CompoundTag rootTag = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
                ListTag processorList = rootTag.getList("list", 10); // 10 = CompoundTag

                for (Tag processorTagRaw : processorList) {
                    if (!(processorTagRaw instanceof CompoundTag processorTag)) {
                        continue;
                    }

                    int processorId = processorTag.getInt("processorId");
                    ListTag outputsTag = processorTag.getList("outputs", 10);

                    List<AutoCraftOutput> outputs = new ArrayList<>();
                    for (Tag outputTagRaw : outputsTag) {
                        if (outputTagRaw instanceof CompoundTag outputTag) {
                            outputs.add(AutoCraftOutput.readFromNBT(outputTag));
                        }
                    }

                    this.craftOutputs.put(processorId, outputs);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.autoCraftSystem = null;
            this.craftOutputs.clear();
            this.index = -1;
            this.processorId = -1;
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(hasSystem);

        if (hasSystem) {
            Processor processor;
            if (index != -1 && processorId != -1) {
                processor = getProcessor();
                if (processor != null) {
                    this.autoCraftSystem = processor.getAutoCraft(index);
                    if (this.autoCraftSystem == null) {
                        index = -1;
                        processorId = -1;
                    }
                } else {
                    index = -1;
                    processorId = -1;
                    this.autoCraftSystem = null;
                }
            }

        customPacketBuffer.writeBoolean(index != -1);
        customPacketBuffer.writeInt(index);
        customPacketBuffer.writeInt(processorId);

        this.craftOutputs.clear();
        ListTag processorList = new ListTag();

        for (Processor processor1 : new ArrayList<>(this.network.getProcessors())) {
            List<AutoCraftOutput> outputs = new ArrayList<>();
            ListTag outputsTag = new ListTag();

            for (AutoCraftSystem autoCraftSystem : processor1.getAutoCrafts()) {
                AutoCraftOutput output = autoCraftSystem.getAutoCraftOutput();
                if (output != null) {
                    outputs.add(output);
                    outputsTag.add(output.writeToNBT());
                }
            }

            this.craftOutputs.put(processor1.getId(), outputs);

            CompoundTag processorTag = new CompoundTag();
            processorTag.putInt("processorId", processor1.getId());
            processorTag.put("outputs", outputsTag);

            processorList.add(processorTag);
        }

        if (index != -1 && getProcessor() != null) {
            this.autoCraftSystem = getProcessor().getAutoCraft(index);
            if (this.autoCraftSystem != null) {
                try {
                    EncoderHandler.encode(customPacketBuffer, this.autoCraftSystem.writeToTag());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            CompoundTag rootTag = new CompoundTag();
            rootTag.put("list", processorList);
            EncoderHandler.encode(customPacketBuffer, rootTag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        } else {
            this.autoCraftSystem = null;
        }

        return customPacketBuffer;
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt = super.writeToNBT(nbt);
        return nbt;

    }

    public Processor getProcessor() {
        return network.getProcessors().get(processorId);
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

    }

    @Override
    public ContainerAutoCraftMonitor getGuiContainer(Player var1) {

        return new ContainerAutoCraftMonitor(this, var1);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenAutoCraftMonitor(getGuiContainer(entityPlayer));
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        addStorageSystemConsumptionInformation(tooltip);
    }

    private void addStorageSystemConsumptionInformation(final List<String> tooltip) {
        tooltip.add(ChatFormatting.GOLD + Localization.translate("iu.storage_system.tooltip.header"));
        tooltip.add(ChatFormatting.GRAY + Localization.translate("iu.storage_system.tooltip.consumption")
                + ": " + ChatFormatting.YELLOW + formatStorageSystemPower(getRequiredPower()) + " "
                + Localization.translate("iu.storage_system.tooltip.unit"));
        tooltip.add(ChatFormatting.DARK_GRAY + Localization.translate("iu.storage_system.tooltip.consumption.description"));
    }

    private static String formatStorageSystemPower(final double value) {
        if (Math.abs(value - Math.rint(value)) < 0.000001D) {
            return String.valueOf((long) Math.rint(value));
        }
        return String.format(Locale.ROOT, "%.2f", value);
    }

    @Override
    public double getRequiredPower() {
        return 1;
    }

    @Override
    public void setStorageNetwork(StorageNetwork network) {
        hasSystem = network != null;
        if (network != null)
            this.network = network;
        else
            this.network = null;
    }


    public void cancelAutoCraft(int idAutoCraft) {
        if (hasSystem && this.getProcessor() != null) {
            getProcessor().cancelAutoCraft(idAutoCraft);
        }
    }


    public void removeCraft(Integer b) {
        if (hasSystem && this.getProcessor() != null) {
            getProcessor().removeCraft(b);
        }
    }

    public void removeNetworkCraft(Integer b) {
        if (hasSystem && this.getProcessor() != null) {
            getProcessor().removeNetworkCraft(b);
        }

    }

    @Override
    public void updateTileServer(Player var1, double var2) {
        if (var2 >= 0)
            if (var2 < 8)
                this.index = (int) var2;
            else
                this.processorId = (int) (var2 - 8);
        else {
            cancelAutoCraft((int) Math.abs(var2) - 1);
        }
    }

    public Map<Integer, List<AutoCraftOutput>> getCraftOutputs() {
        return craftOutputs;
    }
}
