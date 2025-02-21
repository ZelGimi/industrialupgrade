package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerAutomaticMechanism;
import com.denfop.container.SlotInfo;
import com.denfop.gui.GuiAutomaticMechanism;
import com.denfop.invslot.HandlerInventory;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.denfop.utils.ModUtils.getItemHandler;

public class TileEntityAutomaticMechanism extends TileEntityInventory implements IUpdatableTileEvent {

    public final SlotInfo slot;
    public final InvSlot slotOI;
    public final Map<EnumFacing, HandlerInventory> iItemHandlerMap = new HashMap<>();
    public final Map<IItemHandler, Integer> slotHandler = new HashMap<>();
    private final IItemHandler main_handler;
    public Map<EnumFacing, Upgrade> typeUpgradeMap = new HashMap<>();
    public Map<EnumFacing, List<ItemStack>> extract = new HashMap<>();
    public Map<EnumFacing, List<ItemStack>> pulling = new HashMap<>();
    private int type = 0;
    private boolean put = false;

    public TileEntityAutomaticMechanism() {

        this.slot = new SlotInfo(this, 36, false) {
            @Override
            public void put(final int index, final ItemStack stack) {
                super.put(index, stack);
                ((TileEntityAutomaticMechanism) this.base).updateList();
            }
        };
        this.slotOI = new InvSlot(this, InvSlot.TypeItemSlot.INPUT_OUTPUT, 24) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                put = true;
            }
        };
        for (EnumFacing facing1 : EnumFacing.VALUES) {
            typeUpgradeMap.put(facing1, Upgrade.NONE);
        }
        main_handler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getFacing());
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        for (EnumFacing facing1 : EnumFacing.VALUES) {
            customPacketBuffer.writeInt(this.typeUpgradeMap.get(facing1).ordinal());
        }
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);

        for (EnumFacing facing1 : EnumFacing.VALUES) {
            this.typeUpgradeMap.replace(facing1, Upgrade.values()[customPacketBuffer.readInt()]);
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.typeUpgradeMap.clear();
        for (EnumFacing facing1 : EnumFacing.VALUES) {
            this.typeUpgradeMap.put(facing1, Upgrade.values()[nbtTagCompound.getInteger(facing1.name().toLowerCase())]);

        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        for (EnumFacing facing1 : this.typeUpgradeMap.keySet()) {
            nbt.setInteger(facing1.name().toLowerCase(), this.typeUpgradeMap.get(facing1).ordinal());
        }
        return nbt;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.world.isRemote) {
            this.updateList();
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.world.provider.getWorldTime() % 20 == 0) {
            this.iItemHandlerMap.clear();
            slotHandler.clear();
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos pos = this.getPos().offset(facing);
                final TileEntity tile1 = this.getWorld().getTileEntity(pos);
                final IItemHandler handler = getItemHandler(tile1, facing.getOpposite());
                if (!(tile1 instanceof IInventory)) {
                    if (handler == null) {
                        this.iItemHandlerMap.put(facing, null);
                    } else {
                        this.iItemHandlerMap.put(facing, new HandlerInventory(handler, null));
                    }
                } else {
                    this.iItemHandlerMap.put(facing, new HandlerInventory(handler, (IInventory) tile1));
                }

                if (handler != null) {
                    this.slotHandler.put(handler, handler.getSlots());
                }
            }

        }
        if (this.world.provider.getWorldTime() % 3 == 0) {

            for (Map.Entry<EnumFacing, Upgrade> enumFacingTypeUpgradeEntry : this.typeUpgradeMap.entrySet()) {
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

    private void tickPullIn(EnumFacing facing) {
        List<ItemStack> itemStackList = this.pulling.getOrDefault(facing, Collections.emptyList());
        if (facing != null) {

            final HandlerInventory handler = this.iItemHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            if (handler.getInventory() != null && handler.getInventory() instanceof TileEntityInventory) {
                final List<InvSlot> inputs = this.getParent().getInputSlots();
                TileEntityInventory inventory = (TileEntityInventory) handler.getInventory();
                final List<InvSlot> outputs = inventory.getOutputSlots();
                cycle:
                for (InvSlot slot : outputs) {
                    cycle1:
                    for (InvSlot invSlot : inputs) {
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
                                if (invSlot.accepts(output, 0)) {
                                    for (int jj = 0; jj < invSlot.size(); jj++) {
                                        if (output.isEmpty()) {
                                            continue cycle2;
                                        }
                                        ItemStack input = invSlot.get(jj);

                                        if (input.isEmpty()) {
                                            if (invSlot.add(output)) {
                                                slot.put(j, ItemStack.EMPTY);
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
                                            if (stack.isItemEqual(output)) {
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
                                        if (invSlot.accepts(output, j)) {
                                            if (invSlot.add(output)) {
                                                slot.put(j, ItemStack.EMPTY);
                                                output = ItemStack.EMPTY;
                                            }
                                        }
                                    } else {
                                        if (!output.isItemEqual(input)) {
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
                                if (stack.isItemEqual(took)) {
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

    private void tick(EnumFacing facing) {
        List<ItemStack> itemStackList = this.extract.getOrDefault(facing, Collections.emptyList());
        if (facing != null) {

            final HandlerInventory handler = this.iItemHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            int slots = 0;
            if (handler.getInventory() != null && handler.getInventory() instanceof TileEntityInventory) {
                TileEntityInventory inventory = (TileEntityInventory) handler.getInventory();
                InvSlot slot = slotOI;
                cycle1:
                for (InvSlot invSlot : inventory.getInputSlots()) {
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
                                    if (stack.isItemEqual(output)) {
                                        find = true;
                                        break;
                                    }
                                }
                                if (!find) {
                                    continue;
                                }
                            }
                            if (invSlot.accepts(output, 0)) {
                                for (int jj = 0; jj < invSlot.size(); jj++) {
                                    if (output.isEmpty()) {
                                        continue cycle2;
                                    }
                                    ItemStack input = invSlot.get(jj);

                                    if (input.isEmpty()) {
                                        if (invSlot.add(output)) {
                                            slot.put(j, ItemStack.EMPTY);
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
                                        if (stack.isItemEqual(output)) {
                                            find = true;
                                            break;
                                        }
                                    }
                                    if (!find) {
                                        continue cycle3;
                                    }
                                }
                                if (input.isEmpty()) {
                                    if (invSlot.accepts(output, j)) {
                                        if (invSlot.add(output)) {
                                            slot.put(jj, ItemStack.EMPTY);
                                            output = ItemStack.EMPTY;
                                        }
                                    }
                                } else {
                                    if (!output.isItemEqual(input)) {
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
                    InvSlot slot = slotOI;
                    for (int j = 0; j < slot.size(); j++) {
                        ItemStack took = slot.get(j);
                        if (took.isEmpty()) {
                            continue;
                        }
                        boolean find = false;
                        if (!itemStackList.isEmpty()) {
                            for (ItemStack stack : itemStackList) {
                                if (stack.isItemEqual(took)) {
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
                            slot.put(j, ItemStack.EMPTY);
                            insertItem1(handler, took, false, slots);
                        } else if (stack != took) {
                            int col = slot.get(j).getCount() - stack.getCount();
                            slot.get(j).shrink(col);
                            stack.setCount(col);
                            insertItem1(handler, stack, false, slots);
                        }


                    }

                } else {
                    InvSlot slot = slotOI;
                    for (int j = 0; j < slot.size(); j++) {
                        ItemStack took = slot.get(j);
                        if (took.isEmpty()) {
                            continue;
                        }
                        boolean find = false;
                        if (!itemStackList.isEmpty()) {
                            for (ItemStack stack : itemStackList) {
                                if (stack.isItemEqual(took)) {
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
                            slot.put(j, ItemStack.EMPTY);
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
        if (a.isEmpty() || !a.isItemEqual(b) || a.hasTagCompound() != b.hasTagCompound()) {
            return false;
        }

        return (!a.hasTagCompound() || a.getTagCompound().equals(b.getTagCompound()));
    }

    @Nonnull
    public ItemStack insertItem2(HandlerInventory dest1, int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        final IItemHandler dest = dest1.getHandler();
        final IInventory inventory = dest1.getInventory();
        ItemStack stackInSlot;
        try {
            stackInSlot = inventory.getStackInSlot(slot);
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


                if (!inventory.isItemValidForSlot(slot, stack)) {
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
                    inventory.setInventorySlotContents(slot, copy);
                    return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            } else {
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.grow(stackInSlot.getCount());
                    inventory.setInventorySlotContents(slot, copy);
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {


            if (!inventory.isItemValidForSlot(slot, stack)) {
                return stack;
            }
            m = Math.min(stack.getMaxStackSize(), dest.getSlotLimit(slot));
            if (m < stack.getCount()) {
                stack = stack.copy();
                if (!simulate) {
                    inventory.setInventorySlotContents(slot, stack.splitStack(m));

                }
                return stack;
            } else {
                if (!simulate) {
                    try {
                        inventory.setInventorySlotContents(slot, stack);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        dest.insertItem(slot, stack, false);
                    }


                }
                return ItemStack.EMPTY;
            }
        }

    }


    @Override
    public ContainerAutomaticMechanism getGuiContainer(final EntityPlayer var1) {
        return new ContainerAutomaticMechanism(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiAutomaticMechanism(getGuiContainer(var1));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.automatic_mechanism;
    }

    private void updateList() {
        extract.clear();
        pulling.clear();
        for (EnumFacing facing1 : EnumFacing.VALUES) {
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
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        Upgrade typeUpgrade = this.typeUpgradeMap.get(EnumFacing.VALUES[(int) var2]);
        Upgrade newTypeUpgrade = Upgrade.values()[(typeUpgrade.ordinal() + 1) % 4];
        this.typeUpgradeMap.replace(EnumFacing.VALUES[(int) var2], newTypeUpgrade);
        this.updateList();
    }

}

