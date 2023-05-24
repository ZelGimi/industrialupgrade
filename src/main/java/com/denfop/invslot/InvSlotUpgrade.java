package com.denfop.invslot;

import com.denfop.Ic2Items;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.Fluids;
import com.denfop.componets.Redstone;
import com.denfop.componets.TileEntityAdvComponent;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import ic2.api.upgrade.IAugmentationUpgrade;
import ic2.api.upgrade.IEnergyStorageUpgrade;
import ic2.api.upgrade.IFullUpgrade;
import ic2.api.upgrade.IProcessingUpgrade;
import ic2.api.upgrade.IRedstoneSensitiveUpgrade;
import ic2.api.upgrade.ITransformerUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.util.StackUtil;
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
    public int augmentation;
    public int extraProcessTime;
    public double processTimeMultiplier;
    public double extraEnergyDemand;
    public double energyDemandMultiplier;
    public double extraEnergyStorage;
    public double energyStorageMultiplier;
    public int extraTier;
    public int tick = 0;
    public boolean update = false;
    List<InvSlotOutput> slots = new ArrayList<>();
    List<InvSlot> inv_slots = new ArrayList<>();
    private EnumFacing[] facings;
    private List<Redstone.IRedstoneModifier> redstoneModifiers = Collections.emptyList();
    private boolean ejectorUpgrade;
    private boolean fluidEjectorUpgrade;
    private boolean pullingUpgrade;
    private boolean fluidPullingUpgrade;

    public InvSlotUpgrade(
            TileEntityInventory base,
            String name,
            int count
    ) {
        super(base, name, Access.I, count);
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

    private static int applyModifier(int base, int extra, double multiplier) {
        double ret = (double) Math.round(((double) base + (double) extra) * multiplier);
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    private static double applyModifier(double base, double extra, double multiplier) {
        return (double) Math.round((base + extra) * multiplier);
    }

    private static EnumFacing getDirection(ItemStack stack) {
        int rawDir = StackUtil.getOrCreateNbtData(stack).getByte("dir");
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
        List<Redstone.IRedstoneModifier> newRedstoneModifiers = new ArrayList<>();

        for (int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.get(i);
            if (!StackUtil.isEmpty(stack)) {
                IUpgradeItem upgrade = (IUpgradeItem) stack.getItem();
                boolean all = upgrade instanceof IFullUpgrade;
                int size = StackUtil.getSize(stack);
                if (all || upgrade instanceof IAugmentationUpgrade) {
                    this.augmentation += ((IAugmentationUpgrade) upgrade).getAugmentation(stack, block) * size;
                }

                if (all || upgrade instanceof IProcessingUpgrade) {
                    IProcessingUpgrade procUpgrade = (IProcessingUpgrade) upgrade;
                    this.extraProcessTime += procUpgrade.getExtraProcessTime(stack, block) * size;
                    this.processTimeMultiplier *= Math.pow(procUpgrade.getProcessTimeMultiplier(stack, block), size);
                    this.extraEnergyDemand += procUpgrade.getExtraEnergyDemand(stack, block) * size;
                    this.energyDemandMultiplier *= Math.pow(procUpgrade.getEnergyDemandMultiplier(stack, block), size);
                }

                if (all || upgrade instanceof IEnergyStorageUpgrade) {
                    IEnergyStorageUpgrade engUpgrade = (IEnergyStorageUpgrade) upgrade;
                    this.extraEnergyStorage += engUpgrade.getExtraEnergyStorage(stack, block) * size;
                    this.energyStorageMultiplier *= Math.pow(engUpgrade.getEnergyStorageMultiplier(stack, block), size);
                }

                if (all || upgrade instanceof ITransformerUpgrade) {
                    this.extraTier += ((ITransformerUpgrade) upgrade).getExtraTier(stack, block) * size;
                }

            }

        }

        for (final TileEntityAdvComponent component : this.base.getParent().getComps()) {
            if (component instanceof Redstone) {
                Redstone rs = (Redstone) component;
                rs.removeRedstoneModifiers(this.redstoneModifiers);
                rs.addRedstoneModifiers(newRedstoneModifiers);
                rs.update();
            }
        }
        this.redstoneModifiers = newRedstoneModifiers;
        for (int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.get(i);
            if (stack.isItemEqual(Ic2Items.ejectorUpgrade)) {
                this.ejectorUpgrade = true;
                this.facings[i] = getDirection(stack);
            } else if (stack.isItemEqual(Ic2Items.fluidEjectorUpgrade)) {
                this.fluidEjectorUpgrade = true;
                this.facings[i] = getDirection(stack);
            } else if (stack.isItemEqual(Ic2Items.pullingUpgrade)) {
                this.pullingUpgrade = true;
                this.facings[i] = getDirection(stack);
            } else if (stack.isItemEqual(Ic2Items.fluidpullingUpgrade)) {
                this.fluidPullingUpgrade = true;
                this.facings[i] = getDirection(stack);
            }
        }
    }

    private void resetRates() {
        this.augmentation = 0;
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

    public int getOperationsPerTick(int defaultOperationLength) {
        return defaultOperationLength == 0 ? 64 : this.getOpsPerTick(this.getStackOpLen(defaultOperationLength));
    }

    public int getOperationLength(int defaultOperationLength) {
        if (defaultOperationLength == 0) {
            return 1;
        } else {
            double stackOpLen = this.getStackOpLen(defaultOperationLength);
            int opsPerTick = this.getOpsPerTick(stackOpLen);
            return Math.max(1, (int) Math.round(stackOpLen * (double) opsPerTick / 64.0D));
        }
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
        return applyModifier(defaultEnergyDemand, this.extraEnergyDemand, this.energyDemandMultiplier);
    }

    public double getEnergyStorage(int defaultEnergyStorage) {
        return applyModifier(defaultEnergyStorage, this.extraEnergyStorage, this.energyStorageMultiplier);
    }

    public double getEnergyStorage(double defaultEnergyStorage) {
        return applyModifier(defaultEnergyStorage, this.extraEnergyStorage, this.energyStorageMultiplier);
    }

    public int getTier(int defaultTier) {
        return applyModifier(defaultTier, this.extraTier, 1.0D);
    }

    public boolean tickNoMark() {
        IUpgradableBlock block = (IUpgradableBlock) this.base;
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
            if (!StackUtil.isEmpty(stack) && stack.getItem() instanceof IUpgradeItem) {
                update = true;
                if (stack.isItemEqual(Ic2Items.ejectorUpgrade) || stack.isItemEqual(Ic2Items.advejectorUpgrade)) {
                    this.tick(i);
                } else if (stack.isItemEqual(Ic2Items.fluidEjectorUpgrade)) {
                    this.tick_fluid(i);
                } else if (this.tick % 4 == 0 && stack.isItemEqual(Ic2Items.pullingUpgrade) || stack.isItemEqual(Ic2Items.advpullingUpgrade)) {
                    this.tickPullIn(i);
                } else if (this.tick % 4 == 0 && stack.isItemEqual(Ic2Items.fluidpullingUpgrade)) {
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
                    if (ModUtils.insertItem(this.main_handler, took, true, slots).isEmpty()) {
                        took = handler.getHandler().extractItem(j, took.getCount(), false);
                        ModUtils.insertItem(this.main_handler, took, false, slots);
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
                        if (ModUtils.insertItem(this.main_handler, took, true, slots).isEmpty()) {
                            took = handler.getHandler().extractItem(j, took.getCount(), false);
                            ModUtils.insertItem(this.main_handler, took, false, slots);
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


                        if (insertItem1(handler, took, true, slots).isEmpty()) {

                            slot.put(j, ItemStack.EMPTY);
                            insertItem1(handler, took, false, slots);

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
                        if (ModUtils.insertItem(handler.getHandler(), took, true, slots).isEmpty()) {
                            slot.put(j, ItemStack.EMPTY);
                            ModUtils.insertItem(handler.getHandler(), took, false, slots);
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
                            if (insertItem1(handler, took, true, slots).isEmpty()) {
                                slot.put(j, ItemStack.EMPTY);
                                insertItem1(handler, took, false, slots);

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
                            if (ModUtils.insertItem(handler.getHandler(), took, true, slots).isEmpty()) {
                                slot.put(j, ItemStack.EMPTY);
                                ModUtils.insertItem(handler.getHandler(), took, false, slots);
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
            stack = this.insertItem2(dest, i, stack, simulate);

            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
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

            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.grow(stackInSlot.getCount());
                    inventory.setInventorySlotContents(slot, copy);
                    return stack;
                }
            }
            return stack;
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


    private static class UpgradeRedstoneModifier implements Redstone.IRedstoneModifier {

        private final IRedstoneSensitiveUpgrade upgrade;
        private final ItemStack stack;
        private final IUpgradableBlock block;

        UpgradeRedstoneModifier(IRedstoneSensitiveUpgrade upgrade, ItemStack stack, IUpgradableBlock block) {
            this.upgrade = upgrade;
            this.stack = stack.copy();
            this.block = block;
        }

        public int getRedstoneInput(int redstoneInput) {
            return this.upgrade.getRedstoneInput(this.stack, this.block, redstoneInput);
        }

    }

}
