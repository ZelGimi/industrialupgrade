package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuAutomaticMechanism;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.SlotInfo;
import com.denfop.inventory.HandlerInventory;
import com.denfop.inventory.Inventory;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenAutomaticMechanism;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.*;

import static com.denfop.utils.ModUtils.getItemHandler;

public class BlockEntityAutomaticMechanism extends BlockEntityInventory implements IUpdatableTileEvent {

    public final SlotInfo slot;
    public final Inventory slotOI;
    public final Map<Direction, HandlerInventory> iItemHandlerMap = new HashMap<>();
    public final Map<IItemHandler, Integer> slotHandler = new HashMap<>();
    private final IItemHandler main_handler;
    public Map<Direction, Upgrade> typeUpgradeMap = new HashMap<>();
    public Map<Direction, List<ItemStack>> extract = new HashMap<>();
    public Map<Direction, List<ItemStack>> pulling = new HashMap<>();
    private int type = 0;
    private boolean put = false;

    public BlockEntityAutomaticMechanism(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.automatic_mechanism, pos, state);
        this.slot = new SlotInfo(this, 36, false) {
            @Override
            public ItemStack set(final int index, final ItemStack stack) {
                super.set(index, stack);
                ((BlockEntityAutomaticMechanism) this.base).updateList();
                return stack;
            }
        };
        this.slotOI = new Inventory(this, Inventory.TypeItemSlot.INPUT_OUTPUT, 24) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                put = true;
                return content;
            }
        };
        for (Direction facing1 : Direction.values()) {
            typeUpgradeMap.put(facing1, Upgrade.NONE);
        }
        main_handler = this.getCapability(Capabilities.ItemHandler.BLOCK, this.getFacing());
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        for (Direction facing1 : Direction.values()) {
            customPacketBuffer.writeInt(this.typeUpgradeMap.get(facing1).ordinal());
        }
        return customPacketBuffer;
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.automation_mechanism.info"));
        tooltip.add(Localization.translate("iu.automation_mechanism.info1"));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);

        for (Direction facing1 : Direction.values()) {
            this.typeUpgradeMap.replace(facing1, Upgrade.values()[customPacketBuffer.readInt()]);
        }
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.typeUpgradeMap.clear();
        for (Direction facing1 : Direction.values()) {
            this.typeUpgradeMap.put(facing1, Upgrade.values()[nbtTagCompound.getInt(facing1.name().toLowerCase())]);

        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt = super.writeToNBT(nbt);
        for (Direction facing1 : this.typeUpgradeMap.keySet()) {
            nbt.putInt(facing1.name().toLowerCase(), this.typeUpgradeMap.get(facing1).ordinal());
        }
        return nbt;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.level.isClientSide) {
            this.updateList();
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.level.getGameTime() % 20 == 0) {
            this.iItemHandlerMap.clear();
            slotHandler.clear();
            for (Direction facing : Direction.values()) {
                BlockPos pos = this.getPos().offset(facing.getNormal());
                final BlockEntity tile1 = this.getWorld().getBlockEntity(pos);
                final IItemHandler handler = getItemHandler(tile1, facing.getOpposite());
                if (!(tile1 instanceof Container)) {
                    if (handler == null) {
                        this.iItemHandlerMap.put(facing, null);
                    } else {
                        this.iItemHandlerMap.put(facing, new HandlerInventory(handler, null));
                    }
                } else {
                    this.iItemHandlerMap.put(facing, new HandlerInventory(handler, (Container) tile1));
                }

                if (handler != null) {
                    this.slotHandler.put(handler, handler.getSlots());
                }
            }

        }
        if (this.level.getGameTime() % 3 == 0) {

            for (Map.Entry<Direction, Upgrade> enumFacingTypeUpgradeEntry : this.typeUpgradeMap.entrySet()) {
                switch (enumFacingTypeUpgradeEntry.getValue()) {
                    case EXTRACT:
                        this.tick(enumFacingTypeUpgradeEntry.getKey());
                        break;
                    case PULLING:
                        this.tickPullIn(enumFacingTypeUpgradeEntry.getKey());
                        break;
                    case EXT_PUL:
                        this.tick(enumFacingTypeUpgradeEntry.getKey());
                        this.tickPullIn(enumFacingTypeUpgradeEntry.getKey());
                        break;
                }
            }
        }
    }

    private void tickPullIn(Direction facing) {
        List<ItemStack> itemStackList = this.pulling.getOrDefault(facing, Collections.emptyList());
        if (facing != null) {

            final HandlerInventory handler = this.iItemHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            if (handler.getInventory() != null && handler.getInventory() instanceof BlockEntityInventory) {
                final List<Inventory> inputs = this.getInputSlots();
                BlockEntityInventory inventory = (BlockEntityInventory) handler.getInventory();
                final List<Inventory> outputs = inventory.getOutputSlots();
                cycle:
                for (Inventory slot : outputs) {
                    cycle1:
                    for (Inventory invSlot : inputs) {
                        if (invSlot.acceptAllOrIndex()) {
                            cycle2:
                            for (int j = 0; j < slot.size(); j++) {
                                ItemStack output = slot.get(j);
                                if (output.isEmpty() || output.getCount() == output.getMaxStackSize()) {
                                    continue;
                                }
                                boolean find = false;
                                if (!itemStackList.isEmpty()) {
                                    for (ItemStack stack : itemStackList) {
                                        if (ModUtils.checkItemEquality(stack, output)) {
                                            find = true;
                                            break;
                                        }
                                    }
                                    if (!find) {
                                        continue;
                                    }
                                }
                                if (invSlot.canPlaceItem(0, output)) {
                                    for (int jj = 0; jj < invSlot.size(); jj++) {
                                        if (output.isEmpty()) {
                                            continue cycle2;
                                        }
                                        ItemStack input = invSlot.get(jj);

                                        if (input.isEmpty()) {
                                            if (invSlot.add(output)) {
                                                slot.set(j, ItemStack.EMPTY);
                                                output = ItemStack.EMPTY;
                                            }
                                        } else {
                                            if (!ModUtils.checkItemEquality(input, output)) {
                                                continue;
                                            }
                                            int maxCount = Math.min(
                                                    input.getMaxStackSize() - input.getCount(),
                                                    output.getCount()
                                            );
                                            if (maxCount > 0) {
                                                input.grow(maxCount);
                                                output.shrink(maxCount);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            cycle3:
                            for (int jj = 0; jj < slot.size(); jj++) {

                                for (int j = 0; j < invSlot.size(); j++) {
                                    ItemStack output = slot.get(jj);
                                    if (output.isEmpty()) {
                                        continue cycle3;
                                    }
                                    boolean find = false;
                                    if (!itemStackList.isEmpty()) {
                                        for (ItemStack stack : itemStackList) {
                                            if (stack.is(output.getItem())) {
                                                find = true;
                                                break;
                                            }
                                        }
                                        if (!find) {
                                            continue cycle3;
                                        }
                                    }
                                    ItemStack input = invSlot.get(j);

                                    if (input.isEmpty()) {
                                        if (invSlot.canPlaceItem(j, output)) {
                                            if (invSlot.add(output)) {
                                                slot.set(jj, ItemStack.EMPTY);
                                                output = ItemStack.EMPTY;
                                            }
                                        }
                                    } else {
                                        if (!output.is(input.getItem())) {
                                            continue;
                                        }
                                        int maxCount = Math.min(
                                                input.getMaxStackSize() - input.getCount(),
                                                output.getCount()
                                        );
                                        if (maxCount > 0) {
                                            input.grow(maxCount);
                                            output.shrink(maxCount);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                int slots = 0;
                try {
                    slots = this.slotHandler.get(handler.getHandler());
                } catch (Exception ignored) {
                }
                for (int j = 0; j < slots; j++) {
                    ItemStack took = handler.getHandler().extractItem(j, 64, true);

                    if (!took.isEmpty()) {
                        boolean find = false;
                        if (!itemStackList.isEmpty()) {
                            for (ItemStack stack : itemStackList) {
                                if (stack.is(took.getItem())) {
                                    find = true;
                                    break;
                                }
                            }
                            if (!find) {
                                continue;
                            }
                        }
                        final ItemStack took1 = ModUtils.insertItem(this.main_handler, took, true, this.main_handler.getSlots());
                        if (took1.isEmpty()) {
                            took = handler.getHandler().extractItem(j, took.getCount(), false);

                            ModUtils.insertItem(this.main_handler, took, false, this.main_handler.getSlots());
                        } else if (took1 != took) {
                            int count = took1.getCount() - took.getCount();
                            count = Math.abs(count);
                            took = handler.getHandler().extractItem(j, count, false);
                            ModUtils.insertItem(this.main_handler, took, false, this.main_handler.getSlots());
                        }
                    }
                }
            }
        }
    }

    private void tick(Direction facing) {
        List<ItemStack> itemStackList = this.extract.getOrDefault(facing, Collections.emptyList());
        if (facing != null) {

            final HandlerInventory handler = this.iItemHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            int slots = 0;
            if (handler.getInventory() != null && handler.getInventory() instanceof BlockEntityInventory) {
                BlockEntityInventory inventory = (BlockEntityInventory) handler.getInventory();
                Inventory slot = slotOI;
                cycle1:
                for (Inventory invSlot : inventory.getInputSlots()) {
                    if (invSlot.acceptAllOrIndex()) {
                        cycle2:
                        for (int j = 0; j < slot.size(); j++) {
                            ItemStack output = slot.get(j);
                            if (output.isEmpty()) {
                                continue;
                            }
                            boolean find = false;
                            if (!itemStackList.isEmpty()) {
                                for (ItemStack stack : itemStackList) {
                                    if (stack.is(output.getItem())) {
                                        find = true;
                                        break;
                                    }
                                }
                                if (!find) {
                                    continue;
                                }
                            }
                            if (invSlot.canPlaceItem(0, output)) {
                                for (int jj = 0; jj < invSlot.size(); jj++) {
                                    if (output.isEmpty()) {
                                        continue cycle2;
                                    }
                                    ItemStack input = invSlot.get(jj);

                                    if (input.isEmpty()) {
                                        if (invSlot.add(output)) {
                                            slot.set(j, ItemStack.EMPTY);
                                            output = ItemStack.EMPTY;
                                        }
                                    } else {
                                        if (!ModUtils.checkItemEquality(input, output)) {
                                            continue;
                                        }
                                        int maxCount = Math.min(
                                                input.getMaxStackSize() - input.getCount(),
                                                output.getCount()
                                        );
                                        if (maxCount > 0) {
                                            input.grow(maxCount);
                                            output.shrink(maxCount);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        cycle3:
                        for (int jj = 0; jj < slot.size(); jj++) {

                            for (int j = 0; j < invSlot.size(); j++) {
                                ItemStack output = slot.get(jj);
                                ItemStack input = invSlot.get(j);
                                if (output.isEmpty()) {
                                    continue cycle3;
                                }
                                boolean find = false;
                                if (!itemStackList.isEmpty()) {
                                    for (ItemStack stack : itemStackList) {
                                        if (stack.is(output.getItem())) {
                                            find = true;
                                            break;
                                        }
                                    }
                                    if (!find) {
                                        continue cycle3;
                                    }
                                }
                                if (input.isEmpty()) {
                                    if (invSlot.canPlaceItem(j, output)) {
                                        if (invSlot.add(output)) {
                                            slot.set(jj, ItemStack.EMPTY);
                                            output = ItemStack.EMPTY;
                                        }
                                    }
                                } else {
                                    if (!output.is(input.getItem())) {
                                        continue;
                                    }
                                    int maxCount = Math.min(
                                            input.getMaxStackSize() - input.getCount(),
                                            output.getCount()
                                    );
                                    if (maxCount > 0) {
                                        input.grow(maxCount);
                                        output.shrink(maxCount);
                                    }
                                }
                            }
                        }
                    }

                }
            } else {
                try {
                    slots = this.slotHandler.get(handler.getHandler());
                } catch (Exception ignored) {
                }

                if (handler.getInventory() != null) {
                    Inventory slot = slotOI;
                    for (int j = 0; j < slot.size(); j++) {
                        ItemStack took = slot.get(j);
                        if (took.isEmpty()) {
                            continue;
                        }
                        boolean find = false;
                        if (!itemStackList.isEmpty()) {
                            for (ItemStack stack : itemStackList) {
                                if (stack.is(took.getItem())) {
                                    find = true;
                                    break;
                                }
                            }
                            if (!find) {
                                continue;
                            }
                        }
                        final ItemStack stack = insertItem1(handler, took, true, slots);
                        if (stack.isEmpty()) {
                            slot.set(j, ItemStack.EMPTY);
                            insertItem1(handler, took, false, slots);
                        } else if (stack != took) {
                            int col = slot.get(j).getCount() - stack.getCount();
                            slot.get(j).shrink(col);
                            stack.setCount(col);
                            insertItem1(handler, stack, false, slots);
                        }


                    }

                } else {
                    Inventory slot = slotOI;
                    for (int j = 0; j < slot.size(); j++) {
                        ItemStack took = slot.get(j);
                        if (took.isEmpty()) {
                            continue;
                        }
                        boolean find = false;
                        if (!itemStackList.isEmpty()) {
                            for (ItemStack stack : itemStackList) {
                                if (stack.is(took.getItem())) {
                                    find = true;
                                    break;
                                }
                            }
                            if (!find) {
                                continue;
                            }
                        }
                        took = took.copy();
                        final ItemStack stack = ModUtils.insertItem(handler.getHandler(), took, true, slots);
                        if (stack.isEmpty()) {
                            slot.set(j, ItemStack.EMPTY);
                            ModUtils.insertItem(handler.getHandler(), took, false, slots);
                        } else if (stack != took) {
                            int col = slot.get(j).getCount() - stack.getCount();
                            slot.get(j).shrink(col);
                            stack.setCount(col);
                            ModUtils.insertItem(handler.getHandler(), stack, false, slots);
                        }

                    }

                }
            }


        }

    }

    @Nonnull
    public ItemStack insertItem1(HandlerInventory dest, @Nonnull ItemStack stack, boolean simulate, int slot) {
        if (dest == null || stack.isEmpty()) {
            return stack;
        }

        for (int i = 0; i < slot; i++) {
            final ItemStack stack2 = this.insertItem2(dest, i, stack, simulate);

            if (stack2.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (stack2 != stack) {
                return stack2;
            }
        }

        return stack;
    }

    public boolean canItemStacksStack(@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (a.isEmpty() || !a.is(b.getItem()) || a.getComponents().isEmpty() != b.getComponents().isEmpty()) {
            return false;
        }

        return (a.getComponents().isEmpty() || a.getComponents().equals(b.getComponents()));
    }

    @Nonnull
    public ItemStack insertItem2(HandlerInventory dest1, int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        final IItemHandler dest = dest1.getHandler();
        final Container inventory = dest1.getInventory();
        ItemStack stackInSlot;
        try {
            stackInSlot = inventory.getItem(slot);
        } catch (ArrayIndexOutOfBoundsException e) {
            stackInSlot = dest.getStackInSlot(slot);
        }
        int m;
        if (!stackInSlot.isEmpty()) {

            int max = stackInSlot.getMaxStackSize();
            int limit = dest.getSlotLimit(slot);
            if (stackInSlot.getCount() >= Math.min(max, limit)) {
                return stack;
            }
            if (simulate) {


                if (!inventory.canPlaceItem(slot, stack)) {
                    return stack;
                }
            }

            if (!canItemStacksStack(stack, stackInSlot)) {
                return stack;
            }


            m = Math.min(max, limit) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    inventory.setItem(slot, copy);
                    return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            } else {
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(m);
                    copy.grow(stackInSlot.getCount());
                    inventory.setItem(slot, copy);
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {


            if (!inventory.canPlaceItem(slot, stack)) {
                return stack;
            }
            m = Math.min(stack.getMaxStackSize(), dest.getSlotLimit(slot));
            if (m < stack.getCount()) {
                stack = stack.copy();
                if (!simulate) {
                    inventory.setItem(slot, stack.split(m));

                }
                return stack;
            } else {
                if (!simulate) {
                    try {
                        inventory.setItem(slot, stack);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        dest.insertItem(slot, stack, false);
                    }


                }
                return ItemStack.EMPTY;
            }
        }

    }


    @Override
    public ContainerMenuAutomaticMechanism getGuiContainer(final Player var1) {
        return new ContainerMenuAutomaticMechanism(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenAutomaticMechanism((ContainerMenuAutomaticMechanism) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.automatic_mechanism;
    }

    private void updateList() {
        extract.clear();
        pulling.clear();
        for (Direction facing1 : Direction.values()) {
            Upgrade upgrade = this.typeUpgradeMap.get(facing1);
            if (upgrade == Upgrade.NONE) {
                continue;
            }
            List<ItemStack> itemStackList = new LinkedList<>();
            List<ItemStack> itemStackList1 = new LinkedList<>();
            for (int i = facing1.ordinal() * 6; i < facing1.ordinal() * 6 + 6; i++) {
                ItemStack stack = this.slot.get(i);
                if (!stack.isEmpty() && i >= facing1.ordinal() * 6 && i < facing1.ordinal() * 6 + 3) {
                    itemStackList.add(stack);
                } else if (!stack.isEmpty()) {
                    itemStackList1.add(stack);
                }
            }
            switch (upgrade) {
                case EXTRACT:
                    itemStackList.addAll(itemStackList1);
                    this.extract.put(facing1, itemStackList);
                    break;
                case PULLING:
                    itemStackList.addAll(itemStackList1);
                    this.pulling.put(facing1, itemStackList);
                    break;
                case EXT_PUL:

                    this.extract.put(facing1, itemStackList1);
                    this.pulling.put(facing1, itemStackList);
                    break;
            }
        }
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        Upgrade typeUpgrade = this.typeUpgradeMap.get(Direction.values()[(int) var2]);
        Upgrade newTypeUpgrade = Upgrade.values()[(typeUpgrade.ordinal() + 1) % 4];
        this.typeUpgradeMap.replace(Direction.values()[(int) var2], newTypeUpgrade);
        this.updateList();
    }

}

