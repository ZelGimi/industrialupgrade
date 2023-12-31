package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.Redstone;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvSlotUpgrade extends InvSlot {

    private final TileEntityInventory tile;
    private final Map<EnumFacing, HandlerInventory> iItemHandlerMap;
    private final Map<IItemHandler, Integer> slotHandler;
    private final EnumFacing[] enumFacings = EnumFacing.values();
    private final Map<EnumFacing, IFluidHandler> iFluidHandlerMap;
    private final Fluids fluids;
    private final List<Fluids.InternalFluidTank> fluidTankList = new ArrayList<>();
    private final IItemHandler main_handler;
    public boolean isUpdate = false;
    public int extraProcessTime;
    public double processTimeMultiplier;
    public double extraEnergyDemand;
    public double energyDemandMultiplier;
    public double extraEnergyStorage;

    public double operationsPerTick;
    public double operationLength;
    public double energyConsume;
    public double energyStorageMultiplier;
    public int extraTier;
    public int tick = 0;
    public boolean update = false;
    List<InvSlotOutput> slots = new ArrayList<>();
    List<InvSlot> inv_slots = new ArrayList<>();
    private EnumFacing[] facings;
    private boolean ejectorUpgrade;
    private boolean fluidEjectorUpgrade;
    private boolean pullingUpgrade;
    private boolean fluidPullingUpgrade;

    public InvSlotUpgrade(
            TileEntityInventory base,
            int count
    ) {
        super(base, TypeItemSlot.INPUT, count);
        this.resetRates();
        this.facings = new EnumFacing[count];
        base.getInvSlots().forEach(slot -> {
            if (slot instanceof InvSlotOutput) {
                slots.add((InvSlotOutput) slot);
            }
        });
        base.getInvSlots().forEach(slot -> {
            if (slot.canInput()) {
                inv_slots.add(slot);
            }
        });
        main_handler = base.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, base.getFacing());
        fluids = base.getParent().getComp(Fluids.class);
        if (fluids != null) {
            fluids.getAllTanks().forEach(fluidTankList::add);
        }
        this.tile = base;
        this.slotHandler = new HashMap<>();
        this.iItemHandlerMap = new HashMap<>();
        this.iFluidHandlerMap = new HashMap<>();
    }

    private static int applyModifier(int base, int extra) {
        double ret = (double) Math.round(((double) base + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    private static double applyModifier(double base, double extra, double multiplier) {
        return (double) Math.round((base + extra) * multiplier);
    }

    private static EnumFacing getDirection(ItemStack stack) {
        int rawDir = ModUtils.nbt(stack).getByte("dir");
        return rawDir >= 1 && rawDir <= 6 ? EnumFacing.VALUES[rawDir - 1] : null;
    }


    public boolean accepts(ItemStack stack, final int index) {
        Item rawItem = stack.getItem();
        if (!(rawItem instanceof IUpgradeItem)) {
            return false;
        } else {
            IUpgradeItem item = (IUpgradeItem) rawItem;
            return item.isSuitableFor(stack, ((IUpgradableBlock) this.base).getUpgradableProperties());
        }
    }

    public boolean add(List<ItemStack> stacks) {
        return this.add(stacks, false);
    }

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
        }
    }

    public boolean canAdd(List<ItemStack> stacks) {
        boolean can = true;
        for (ItemStack stack : stacks) {
            can = can && this.canAdd(stack);
        }
        return can;
    }

    public boolean canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), true);
        }
    }

    private boolean add(List<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                for (int i = 0; i < this.size(); i++) {
                    if (this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.put(i, stack.copy());

                        }
                        return true;
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (stack.getTagCompound() != null &&
                                            stack.getTagCompound().equals(this.get(i).getTagCompound())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());

                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public void onChanged() {
        this.resetRates();
        IUpgradableBlock block = (IUpgradableBlock) this.base;

        for (int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.get(i);
            if (!ModUtils.isEmpty(stack)) {
                IUpgradeItem upgrade = (IUpgradeItem) stack.getItem();
                int size = ModUtils.getSize(stack);
                this.extraProcessTime += size;
                this.processTimeMultiplier *= Math.pow(upgrade.getProcessTimeMultiplier(stack), size);
                this.extraEnergyDemand += size;
                this.energyDemandMultiplier *= Math.pow(upgrade.getEnergyDemandMultiplier(stack), size);
                this.extraEnergyStorage += upgrade.getExtraEnergyStorage(stack) * size;
                this.energyStorageMultiplier *= Math.pow(1, size);
                this.extraTier += upgrade.getExtraTier(stack) * size;
            }

        }

        for (final AbstractComponent component : this.base.getParent().getComps()) {
            if (component instanceof Redstone) {
                Redstone rs = (Redstone) component;
                rs.update();
            }
        }
        for (int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.get(i);
            if (stack.isItemEqual(IUItem.ejectorUpgrade)) {
                this.ejectorUpgrade = true;
                this.facings[i] = getDirection(stack);
            } else if (stack.isItemEqual(IUItem.fluidEjectorUpgrade)) {
                this.fluidEjectorUpgrade = true;
                this.facings[i] = getDirection(stack);
            } else if (stack.isItemEqual(IUItem.pullingUpgrade)) {
                this.pullingUpgrade = true;
                this.facings[i] = getDirection(stack);
            } else if (stack.isItemEqual(IUItem.fluidpullingUpgrade)) {
                this.fluidPullingUpgrade = true;
                this.facings[i] = getDirection(stack);
            }
        }
    }

    private void resetRates() {
        this.extraProcessTime = 0;
        this.processTimeMultiplier = 1.0D;
        this.extraEnergyDemand = 0;
        this.energyDemandMultiplier = 1.0D;
        this.extraEnergyStorage = 0;
        this.energyStorageMultiplier = 1.0D;
        this.extraTier = 0;
        this.ejectorUpgrade = false;
        this.fluidEjectorUpgrade = false;
        this.fluidPullingUpgrade = false;
        this.pullingUpgrade = false;
        this.facings = new EnumFacing[this.size()];
    }

    public int getOperationsPerTick1(int defaultOperationLength) {
        if (this.isUpdate) {
            this.operationsPerTick = defaultOperationLength == 0 ? 64 :
                    this.getOpsPerTick(this.getStackOpLen(defaultOperationLength));

        }
        return (int) this.operationsPerTick;
    }

    public int getOperationLength1(int defaultOperationLength) {
        if (this.isUpdate) {
            if (defaultOperationLength == 0) {
                this.operationLength = 1;
            } else {
                double stackOpLen = this.getStackOpLen(defaultOperationLength);
                int opsPerTick = this.getOpsPerTick(stackOpLen);
                this.operationLength = Math.max(1, (int) Math.round(stackOpLen * (double) opsPerTick / 64.0D));
            }
        }
        return (int) this.operationLength;
    }

    public int getOperationsPerTick(int defaultOperationLength) {
        this.operationsPerTick = defaultOperationLength == 0 ? 64 :
                this.getOpsPerTick(this.getStackOpLen(defaultOperationLength));

        return (int) this.operationsPerTick;
    }

    public int getOperationLength(int defaultOperationLength) {
        if (defaultOperationLength == 0) {
            this.operationLength = 1;
        } else {
            double stackOpLen = this.getStackOpLen(defaultOperationLength);
            int opsPerTick = this.getOpsPerTick(stackOpLen);
            this.operationLength = Math.max(1, (int) Math.round(stackOpLen * (double) opsPerTick / 64.0D));
        }

        return (int) this.operationLength;
    }

    private double getStackOpLen(int defaultOperationLength) {
        return ((double) defaultOperationLength + (double) this.extraProcessTime) * 64.0D * this.processTimeMultiplier;
    }

    private int getOpsPerTick(double stackOpLen) {
        return (int) Math.min(Math.ceil(64.0D / stackOpLen), 2.147483647E9D);
    }

    public double getEnergyDemand(int defaultEnergyDemand) {
        return applyModifier(defaultEnergyDemand, this.extraEnergyDemand, this.energyDemandMultiplier);
    }

    public double getEnergyDemand(double defaultEnergyDemand) {
        this.energyConsume = applyModifier(defaultEnergyDemand, this.extraEnergyDemand, this.energyDemandMultiplier);

        return this.energyConsume;
    }

    public double getEnergyDemand1(double defaultEnergyDemand) {
        if (this.isUpdate) {
            this.energyConsume = applyModifier(defaultEnergyDemand, this.extraEnergyDemand, this.energyDemandMultiplier);
        }
        return this.energyConsume;
    }

    public double getEnergyStorage(int defaultEnergyStorage) {
        return applyModifier(defaultEnergyStorage, this.extraEnergyStorage, this.energyStorageMultiplier);
    }

    public double getEnergyStorage(double defaultEnergyStorage) {
        return applyModifier(defaultEnergyStorage, this.extraEnergyStorage, this.energyStorageMultiplier);
    }

    public int getTier(int defaultTier) {
        return applyModifier(defaultTier, this.extraTier);
    }

    public boolean tickNoMark() {
        boolean ret = false;
        this.tick++;
        if (this.tick % 20 == 0) {
            if (this.ejectorUpgrade || pullingUpgrade) {
                this.iItemHandlerMap.clear();
                slotHandler.clear();
                for (EnumFacing facing : enumFacings) {
                    BlockPos pos = this.tile.getPos().offset(facing);
                    final TileEntity tile1 = this.tile.getWorld().getTileEntity(pos);
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
            if (this.fluidEjectorUpgrade || this.fluidPullingUpgrade) {
                this.iFluidHandlerMap.clear();
                for (EnumFacing facing : enumFacings) {
                    BlockPos pos = this.tile.getPos().offset(facing);
                    final TileEntity tile1 = this.tile.getWorld().getTileEntity(pos);
                    final IFluidHandler handler = getFluidHandler(tile1, facing.getOpposite());
                    this.iFluidHandlerMap.put(facing, handler);
                }
            }
            this.tick = 0;
        }
        boolean update = false;
        for (int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.get(i);
            if (!ModUtils.isEmpty(stack) && stack.getItem() instanceof IUpgradeItem) {
                update = true;
                if (this.tick % 2 == 0 && stack.isItemEqual(IUItem.ejectorUpgrade)) {
                    this.tick(i);
                } else if (this.tick % 2 == 0 && stack.isItemEqual(IUItem.fluidEjectorUpgrade)) {
                    this.tick_fluid(i);
                } else if (this.tick % 4 == 0 && stack.isItemEqual(IUItem.pullingUpgrade)) {
                    this.tickPullIn(i);
                } else if (this.tick % 4 == 0 && stack.isItemEqual(IUItem.fluidpullingUpgrade)) {
                    this.tickPullIn_fluid(i);
                }
                ret = true;
            }
        }
        if (this.update != update) {
            this.update = update;
            return true;
        }
        return ret;
    }

    private void tickPullIn_fluid(int i) {
        EnumFacing facing = this.facings[i];
        if (facing != null) {

            final IFluidHandler handler = this.iFluidHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            if (this.fluids == null) {
                return;
            }
            if (handler.drain(Integer.MAX_VALUE, false) == null) {
                return;
            }
            for (IFluidTankProperties fluidTankProperties : handler.getTankProperties()) {
                if (fluidTankProperties.getContents() != null) {
                    for (Fluids.InternalFluidTank tank : this.fluidTankList) {
                        if (tank.getFluidAmount() >= tank.getCapacity()) {
                            continue;
                        }
                        final FluidStack fluid = handler.drain(fluidTankProperties.getContents(), false);
                        if (fluid != null && fluid.amount > 0 && tank.canFillFluidType(fluid)) {
                            tank.fill(handler.drain(fluidTankProperties.getContents(), true), true);
                        }
                    }
                }
            }
        } else {
            for (EnumFacing facing1 : enumFacings) {
                final IFluidHandler handler = this.iFluidHandlerMap.get(facing1);
                if (handler == null) {
                    continue;
                }
                if (handler.drain(Integer.MAX_VALUE, false) == null) {
                    continue;
                }
                if (this.fluids == null) {
                    return;
                }

                for (IFluidTankProperties fluidTankProperties : handler.getTankProperties()) {
                    if (fluidTankProperties.getContents() != null) {
                        for (Fluids.InternalFluidTank tank : this.fluidTankList) {
                            if (tank.getFluidAmount() >= tank.getCapacity()) {
                                continue;
                            }
                            final FluidStack fluid = handler.drain(fluidTankProperties.getContents(), false);
                            if (fluid != null && fluid.amount > 0 && tank.acceptsFluid(fluid.getFluid())) {
                                tank.fill(handler.drain(fluidTankProperties.getContents(), true), true);
                            }
                        }
                    }
                }
            }
        }
    }

    public IItemHandler getItemHandler(@Nullable TileEntity tile, EnumFacing side) {
        if (tile == null) {
            return null;
        }

        IItemHandler handler = tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) ? tile.getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                side
        ) : null;

        if (handler == null) {
            if (side != null && tile instanceof ISidedInventory) {
                handler = new SidedInvWrapper((ISidedInventory) tile, side);
            } else if (tile instanceof IInventory) {
                handler = new InvWrapper((IInventory) tile);
            }
        }

        return handler;
    }

    public IFluidHandler getFluidHandler(@Nullable TileEntity tile, EnumFacing side) {
        if (tile == null) {
            return null;
        }

        return tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side) ? tile.getCapability(
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                side
        ) : null;
    }

    public boolean canItemStacksStack(@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (a.isEmpty() || !a.isItemEqual(b) || a.hasTagCompound() != b.hasTagCompound()) {
            return false;
        }

        return (!a.hasTagCompound() || a.getTagCompound().equals(b.getTagCompound()));
    }

    private void tickPullIn(int i) {
        EnumFacing facing = this.facings[i];
        if (facing != null) {

            final HandlerInventory handler = this.iItemHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            int slots = 0;
            try {
                slots = this.slotHandler.get(handler.getHandler());
            } catch (Exception ignored) {
            }
            for (int j = 0; j < slots; j++) {
                ItemStack took = handler.getHandler().extractItem(j, 64, true);
                if (!took.isEmpty()) {
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
        } else {
            for (EnumFacing facing1 : enumFacings) {
                final HandlerInventory handler = this.iItemHandlerMap.get(facing1);

                if (handler == null) {
                    continue;
                }
                int slots = 0;
                try {
                    slots = this.slotHandler.get(handler.getHandler());
                } catch (Exception ignored) {
                }

                for (int j = 0; j < slots; j++) {
                    ItemStack took = handler.getHandler().extractItem(j, 64, true);

                    if (!took.isEmpty()) {
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

    private void tick(final int i) {
        EnumFacing facing = this.facings[i];
        if (facing != null) {

            final HandlerInventory handler = this.iItemHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            int slots = 0;
            try {
                slots = this.slotHandler.get(handler.getHandler());
            } catch (Exception ignored) {
            }
            if (handler.getInventory() != null) {
                for (InvSlotOutput slot : this.slots) {
                    for (int j = 0; j < slot.size(); j++) {
                        ItemStack took = slot.get(j);
                        if (took.isEmpty()) {
                            continue;
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
                }
            } else {
                for (InvSlotOutput slot : this.slots) {
                    for (int j = 0; j < slot.size(); j++) {
                        ItemStack took = slot.get(j);
                        if (took.isEmpty()) {
                            continue;
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
        } else {
            for (EnumFacing facing1 : enumFacings) {
                final HandlerInventory handler = this.iItemHandlerMap.get(facing1);
                if (handler == null) {
                    continue;
                }

                int slots = 0;
                try {
                    slots = this.slotHandler.get(handler.getHandler());
                } catch (Exception ignored) {
                }
                if (handler.getInventory() != null) {
                    for (InvSlotOutput slot : this.slots) {
                        for (int j = 0; j < slot.size(); j++) {
                            ItemStack took = slot.get(j);
                            if (took.isEmpty()) {
                                continue;
                            }
                            final ItemStack stack = insertItem1(handler, took, true, slots);
                            if (stack.isEmpty()) {
                                slot.put(j, ItemStack.EMPTY);
                                insertItem1(handler, took, false, slots);
                            } else if (stack != took) {
                                slot.get(j).shrink(stack.getCount());
                                insertItem1(handler, stack, false, slots);
                            }


                        }
                    }
                } else {
                    for (InvSlotOutput slot : this.slots) {
                        for (int j = 0; j < slot.size(); j++) {
                            ItemStack took = slot.get(j);
                            if (took.isEmpty()) {
                                continue;
                            }
                            took = took.copy();
                            final ItemStack stack = ModUtils.insertItem(handler.getHandler(), took, true, slots);
                            if (stack.isEmpty()) {
                                slot.put(j, ItemStack.EMPTY);
                                ModUtils.insertItem(handler.getHandler(), took, false, slots);
                            } else if (stack != took) {
                                slot.get(j).shrink(stack.getCount());
                                ModUtils.insertItem(handler.getHandler(), stack, false, slots);
                            }

                        }
                    }
                }
            }
        }
    }

    private void tick_fluid(final int i) {
        EnumFacing facing = this.facings[i];
        if (facing != null) {

            final IFluidHandler handler = this.iFluidHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            if (this.fluids == null) {
                return;
            }
            for (Fluids.InternalFluidTank tank : this.fluidTankList) {
                if (tank.getFluidAmount() <= 0) {
                    continue;
                }
                int amount = handler.fill(tank.getFluid(), false);
                if (amount > 0 && tank.canDrain(facing)) {
                    tank.drain(handler.fill(tank.getFluid(), true), true);
                }
            }
        } else {
            for (EnumFacing facing1 : enumFacings) {
                final IFluidHandler handler = this.iFluidHandlerMap.get(facing1);
                if (handler == null) {
                    continue;
                }

                if (this.fluids == null) {
                    return;
                }
                for (Fluids.InternalFluidTank tank : this.fluidTankList) {
                    if (tank.getFluidAmount() <= 0) {
                        continue;
                    }
                    int amount = handler.fill(tank.getFluid(), false);
                    if (amount > 0 && tank.canDrain(facing1)) {
                        tank.drain(handler.fill(tank.getFluid(), true), true);
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


    @Nonnull
    public ItemStack insertItem2(HandlerInventory dest1, int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        final IItemHandler dest = dest1.getHandler();
        final IInventory inventory = dest1.getInventory();
        ItemStack stackInSlot;
        int maxSlots = dest.getSlots();
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
                // copy the stack to not modify the original one
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
                // copy the stack to not modify the original one
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


    public double getEnergyStorage(int defaultEnergyStorage, int defaultOperationLength, int defaultEnergyDemand) {
        int opLen = this.getOperationLength(defaultOperationLength);
        double energyDemand = this.getEnergyDemand(defaultEnergyDemand);
        return applyModifier(defaultEnergyStorage, this.extraEnergyStorage + opLen * energyDemand, this.energyStorageMultiplier);
    }

}
