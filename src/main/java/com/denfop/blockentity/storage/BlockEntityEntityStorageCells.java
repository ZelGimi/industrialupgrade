package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.storage.StorageDeviceCell;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.api.storage.cell.ICell;
import com.denfop.api.storage.cell.ICellItem;
import com.denfop.api.storage.cell.TypeCell;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerStorageCells;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenStorageCells;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Locale;

public class BlockEntityEntityStorageCells extends BlockEntityInventory implements StorageDeviceCell {

    private final Inventory slotSlots;
    public double maxValue = 0;
    public double value = 0;
    protected ComponentBaseEnergy energy;
    Map<Integer, ICell> cellsMap = new HashMap<>();
    List<ICell> itemCells = new LinkedList<>();
    List<ICell> fluidCells = new LinkedList<>();
    boolean reBuild = false;
    private StorageNetwork network;

    public BlockEntityEntityStorageCells(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.storage_cells, pos, state);

        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));
        this.slotSlots = new Inventory(this, Inventory.TypeItemSlot.INPUT, 25) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
                return stack.getItem() instanceof ICellItem;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.STORAGE_CELL;
            }

            @Override
            public ItemStack set(int i, ItemStack empty) {
                ItemStack stack = super.set(i, empty);
                if (stack.isEmpty()) {
                    ICell cell = cellsMap.remove(i);
                    if (cell != null)
                        if (cell.getCellInfo().typeCell() == TypeCell.FLUID)
                            fluidCells.remove(cell);
                        else
                            itemCells.remove(cell);
                } else {
                    ICellItem cellItem = (ICellItem) stack.getItem();
                    ICell cell = cellItem.getCell(stack);
                    if (!cellsMap.containsKey(i)) {
                        cellsMap.put(i, cell);
                    } else {
                        ICell cell1 = cellsMap.remove(i);
                        cellsMap.put(i, cell);
                        if (cell1 != null)
                            if (cell1.getCellInfo().typeCell() == TypeCell.FLUID)
                                fluidCells.remove(cell1);
                            else
                                itemCells.remove(cell1);
                    }
                    if (cell.getCellInfo().typeCell() == TypeCell.FLUID)
                        fluidCells.add(cell);
                    else
                        itemCells.add(cell);
                }
                reBuild = true;
                return stack;
            }
        };
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (ForgeCapabilities.ITEM_HANDLER == cap)
            return LazyOptional.empty();
        return super.getCapability(cap, facing);
    }
    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.storage_cells;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }

    public Inventory getSlots() {
        return slotSlots;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        value = customPacketBuffer.readDouble();
        maxValue = customPacketBuffer.readDouble();

    }


    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        packet.writeDouble(this.value);
        packet.writeDouble(this.maxValue);
        return packet;
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

    public void updateEntityServer() {
        super.updateEntityServer();
        if (network == null)
            return;
        value = 0;
        maxValue = 0;
        for (Map.Entry<Integer, ICell> cellEntry : cellsMap.entrySet()) {
            ICell cell = cellEntry.getValue();
            value += cell.getStorage();
            maxValue += cell.getCellInfo().capacity();
            if (cell.needUpdate()) {
                cell.save();
                cell.setUpdate(false);

            }
        }
        if (reBuild) {
            reBuild = false;
            network.reworkCell = true;
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {

        super.updateField(name, is);
    }


    public ContainerStorageCells getGuiContainer(Player entityPlayer) {
        return new ContainerStorageCells(this, entityPlayer);
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenStorageCells((ContainerStorageCells) isAdmin);
    }


    public void onLoaded() {
        super.onLoaded();
        for (int i = 0; i < 25; i++) {
            ItemStack stack = slotSlots.getItem(i);
            if (stack.isEmpty()) {
                ICell cell = cellsMap.remove(i);
                if (cell != null)
                    if (cell.getCellInfo().typeCell() == TypeCell.FLUID)
                        fluidCells.remove(cell);
                    else
                        itemCells.remove(cell);
            } else {
                ICellItem cellItem = (ICellItem) stack.getItem();
                ICell cell = cellItem.getCell(stack);
                if (!cellsMap.containsKey(i)) {
                    cellsMap.put(i, cell);
                } else {
                    ICell cell1 = cellsMap.remove(i);
                    cellsMap.put(i, cell);
                    if (cell1 != null)
                        if (cell1.getCellInfo().typeCell() == TypeCell.FLUID)
                            fluidCells.remove(cell1);
                        else
                            itemCells.remove(cell1);
                }
                if (cell.getCellInfo().typeCell() == TypeCell.FLUID)
                    fluidCells.add(cell);
                else
                    itemCells.add(cell);
            }
        }
    }


    @Override
    public double getRequiredPower() {
        return 1;
    }

    @Override
    public void setStorageNetwork(StorageNetwork network) {
        this.network = network;
    }

    @Override
    public List<ICell> getFluidCells() {
        return fluidCells;
    }

    @Override
    public List<ICell> getItemCells() {
        return itemCells;
    }
}
