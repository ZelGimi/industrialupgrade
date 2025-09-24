package com.denfop.inventory;

import com.denfop.IUItem;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.UpgradeItem;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.base.FakePlayerSpawner;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.Redstone;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.ItemStackUpgradeModules;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryUpgrade extends Inventory implements ITypeSlot {

    private final BlockEntityInventory tile;
    private final Map<Direction, HandlerInventory> iItemHandlerMap;
    private final Map<IItemHandler, Integer> slotHandler;
    private final Direction[] enumFacings = Direction.values();
    private final Map<Direction, IFluidHandler> iFluidHandlerMap;
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
    List<InventoryOutput> slots = new ArrayList<>();
    List<Inventory> inv_slots = new ArrayList<>();
    private Direction[] facings;
    private List<List<ItemStack>> whiteList;
    private List<List<Fluid>> whiteList1;
    private boolean ejectorUpgrade;
    private boolean fluidEjectorUpgrade;
    private boolean pullingUpgrade;
    private boolean fluidPullingUpgrade;

    public InventoryUpgrade(
            BlockEntityInventory base,
            int count
    ) {
        super(base, TypeItemSlot.INPUT, count);
        this.resetRates();
        this.facings = new Direction[count];
        this.whiteList = new ArrayList<>(Collections.nCopies(count, Collections.emptyList()));
        this.whiteList1 = new ArrayList<>(Collections.nCopies(count, Collections.emptyList()));

        base.getInvSlots().forEach(slot -> {
            if (slot instanceof InventoryOutput) {
                slots.add((InventoryOutput) slot);
            }
        });
        base.getInvSlots().forEach(slot -> {
            if (slot.canInput()) {
                inv_slots.add(slot);
            }
        });
        main_handler = base.getCapability(ForgeCapabilities.ITEM_HANDLER, base.getFacing()).orElse(null);
        fluids = base.getComp(Fluids.class);
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

    public static Direction getDirection(ItemStack stack) {
        int rawDir = ModUtils.nbt(stack).getByte("dir");
        return rawDir >= 1 && rawDir <= 6 ? Direction.values()[rawDir - 1] : null;
    }

    @Override
    public EnumTypeSlot getTypeSlot(int slotid) {


        return EnumTypeSlot.UPGRADE;

    }

    public boolean canPlaceItem(final int index, ItemStack stack) {
        Item rawItem = stack.getItem();
        if (!(rawItem instanceof UpgradeItem)) {
            return false;
        } else {
            UpgradeItem item = (UpgradeItem) rawItem;
            return item.isSuitableFor(stack, ((BlockEntityUpgrade) this.base).getUpgradableProperties());
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


    public void setChanged() {
        this.resetRates();

        for (int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.get(i);
            if (!ModUtils.isEmpty(stack)) {
                UpgradeItem upgrade = (UpgradeItem) stack.getItem();
                int size = ModUtils.getSize(stack);
                if (upgrade.getProcessTimeMultiplier(stack) < 1) {
                    this.extraProcessTime += size;
                } else {
                    extraProcessTime = 0;
                }
                this.processTimeMultiplier *= Math.pow(upgrade.getProcessTimeMultiplier(stack), size);
                if (upgrade.getProcessTimeMultiplier(stack) < 1) {
                    this.extraEnergyDemand += size;
                } else {
                    extraEnergyDemand = 0;
                }
                this.energyDemandMultiplier *= Math.pow(upgrade.getEnergyDemandMultiplier(stack), size) * operationsPerTick;
                this.extraEnergyStorage += upgrade.getExtraEnergyStorage(stack) * size;
                this.energyStorageMultiplier *= Math.pow(1, size);
                this.extraTier += upgrade.getExtraTier(stack) * size;
            }

        }

        for (final AbstractComponent component : ((BlockEntityInventory) this.base).getComps()) {
            if (component instanceof Redstone) {
                Redstone rs = (Redstone) component;
                rs.update();
            }
        }
        if (this.tile.getWorld() instanceof ServerLevel)
            for (int i = 0; i < this.size(); ++i) {
                ItemStack stack = this.get(i);
                if (stack.is(IUItem.ejectorUpgrade.getItem())) {
                    this.ejectorUpgrade = true;
                    this.facings[i] = getDirection(stack);
                    IItemStackInventory inventory = (IItemStackInventory) stack.getItem();
                    List<ItemStack> stacks =
                            Arrays.asList(((ItemStackUpgradeModules) (inventory.getInventory(new FakePlayerSpawner(this.tile.getWorld()), stack))).getInventory());
                    stacks = stacks.stream()
                            .filter(stack1 -> !stack1.isEmpty())
                            .collect(Collectors.toList());
                    this.whiteList.set(i, stacks);
                } else if (stack.is(IUItem.fluidEjectorUpgrade.getItem())) {
                    this.fluidEjectorUpgrade = true;
                    this.facings[i] = getDirection(stack);
                    IItemStackInventory inventory = (IItemStackInventory) stack.getItem();
                    List<FluidStack> fluidStacks = ((ItemStackUpgradeModules) inventory.getInventory(new FakePlayerSpawner(this.tile.getWorld()), stack)).fluidStackList;
                    List<Fluid> fluidStacks1 = new ArrayList<>();

                    for (FluidStack stacks : fluidStacks) {
                        if (stacks != null) {
                            fluidStacks1.add(stacks.getFluid());
                        }
                    }
                    this.whiteList1.set(i, fluidStacks1);
                } else if (stack.is(IUItem.pullingUpgrade.getItem())) {
                    this.pullingUpgrade = true;
                    this.facings[i] = getDirection(stack);
                    IItemStackInventory inventory = (IItemStackInventory) stack.getItem();
                    List<ItemStack> stacks =
                            Arrays.asList(((ItemStackUpgradeModules) (inventory.getInventory(new FakePlayerSpawner(this.tile.getWorld()), stack))).getInventory());
                    stacks = stacks.stream()
                            .filter(stack1 -> !stack1.isEmpty())
                            .collect(Collectors.toList());
                    this.whiteList.set(i, stacks);
                } else if (stack.is(IUItem.fluidpullingUpgrade.getItem())) {
                    this.fluidPullingUpgrade = true;
                    this.facings[i] = getDirection(stack);
                    IItemStackInventory inventory = (IItemStackInventory) stack.getItem();
                    List<FluidStack> fluidStacks = ((ItemStackUpgradeModules) inventory.getInventory(new FakePlayerSpawner(this.tile.getWorld()), stack)).fluidStackList;
                    List<Fluid> fluidStacks1 = new ArrayList<>();

                    for (FluidStack stacks : fluidStacks) {
                        fluidStacks1.add(stacks.getFluid());
                    }
                    this.whiteList1.set(i, fluidStacks1);
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
        this.facings = new Direction[this.size()];
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
                for (Direction facing : enumFacings) {
                    BlockPos pos = this.tile.getBlockPos().offset(facing.getNormal());
                    final BlockEntity tile1 = this.tile.getLevel().getBlockEntity(pos);
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
            if (this.fluidEjectorUpgrade || this.fluidPullingUpgrade) {
                this.iFluidHandlerMap.clear();
                for (Direction facing : enumFacings) {
                    BlockPos pos = this.tile.getBlockPos().offset(facing.getNormal());
                    final BlockEntity tile1 = this.tile.getLevel().getBlockEntity(pos);
                    final IFluidHandler handler = getFluidHandler(tile1, facing.getOpposite());
                    this.iFluidHandlerMap.put(facing, handler);
                }
            }
            this.tick = 0;
        }
        boolean update = false;
        for (int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.get(i);
            if (!ModUtils.isEmpty(stack) && stack.getItem() instanceof UpgradeItem) {
                update = true;
                if (this.tick % 2 == 0 && stack.is(IUItem.ejectorUpgrade.getItem())) {
                    this.tick(i);
                } else if (this.tick % 2 == 0 && stack.is(IUItem.fluidEjectorUpgrade.getItem())) {
                    this.tick_fluid(i);
                } else if (this.tick % 2 == 0 && stack.is(IUItem.pullingUpgrade.getItem())) {
                    this.tickPullIn(i);
                } else if (this.tick % 2 == 0 && stack.is(IUItem.fluidpullingUpgrade.getItem())) {
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
        Direction facing = this.facings[i];
        final List<Fluid> itemStackList = this.whiteList1.get(i);
        if (facing != null) {

            final IFluidHandler handler = this.iFluidHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            if (this.fluids == null) {
                return;
            }
            if (handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                return;
            }
            for (int ii = 0; ii < handler.getTanks(); ii++) {
                @NotNull FluidStack fluidTankProperties = handler.getFluidInTank(ii);
                if (!fluidTankProperties.copy().isEmpty()) {
                    for (Fluids.InternalFluidTank tank : this.fluidTankList) {
                        if (tank.getFluidAmount() >= tank.getCapacity()) {
                            continue;
                        }
                        final FluidStack fluid = handler.drain(fluidTankProperties.copy(), IFluidHandler.FluidAction.SIMULATE);
                        if (!fluid.isEmpty() && fluid.getAmount() > 0 && tank.canDrain(facing.getOpposite()) && tank.isFluidValid(fluid) && (itemStackList.isEmpty() || itemStackList.contains(
                                fluid.getFluid()))) {
                            FluidStack fluidStack = fluidTankProperties.copy();
                            fluidStack.setAmount(Math.min( tank.getCapacity() - tank.getFluidAmount(),fluidStack.getAmount()));
                            handler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                            tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        } else {
            for (Direction facing1 : enumFacings) {
                final IFluidHandler handler = this.iFluidHandlerMap.get(facing1);
                if (handler == null) {
                    continue;
                }
                if (handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                    continue;
                }
                if (this.fluids == null) {
                    return;
                }

                for (int ii = 0; ii < handler.getTanks(); ii++) {
                    @NotNull FluidStack fluidTankProperties = handler.getFluidInTank(ii);
                    if (!fluidTankProperties.copy().isEmpty()) {
                        for (Fluids.InternalFluidTank tank : this.fluidTankList) {
                            if (tank.getFluidAmount() >= tank.getCapacity()) {
                                continue;
                            }
                            final FluidStack fluid = handler.drain(fluidTankProperties.copy(), IFluidHandler.FluidAction.SIMULATE);
                            if (!fluid.isEmpty() && fluid.getAmount() > 0 && tank.acceptsFluid(fluid.getFluid()) && (itemStackList.isEmpty() || itemStackList.contains(
                                    fluid.getFluid()))) {
                                FluidStack fluidStack = fluidTankProperties.copy();
                                fluidStack.setAmount(Math.min( tank.getCapacity() - tank.getFluidAmount(),fluidStack.getAmount()));
                                handler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                                tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                            }
                        }
                    }
                }
            }
        }
    }

    public IItemHandler getItemHandler(@Nullable BlockEntity tile, Direction side) {
        if (tile == null) {
            return null;
        }

        IItemHandler handler = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, side).orElse(null);

        if (handler == null) {
            if (side != null && tile instanceof WorldlyContainer) {
                handler = new SidedInvWrapper((WorldlyContainer) tile, side);
            } else if (tile instanceof Container) {
                handler = new InvWrapper((Container) tile);
            }
        }

        return handler;
    }

    public IFluidHandler getFluidHandler(@Nullable BlockEntity tile, Direction side) {
        if (tile == null) {
            return null;
        }

        return tile.getCapability(
                ForgeCapabilities.FLUID_HANDLER,
                side
        ).orElse(null);
    }

    public boolean canItemStacksStack(@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (a.isEmpty() || !(a.getItem() == b.getItem()) || a.hasTag() != b.hasTag()) {
            return false;
        }

        return (!a.hasTag() || a.getTag().equals(b.getTag()));
    }

    private void tickPullIn(int i) {
        Direction facing = this.facings[i];
        final List<ItemStack> itemStackList = this.whiteList.get(i);
        if (facing != null) {

            final HandlerInventory handler = this.iItemHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            if (handler.getInventory() != null && handler.getInventory() instanceof BlockEntityInventory) {
                final List<Inventory> inputs = ((BlockEntityInventory) this.base).getInputSlots();
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
                                if (output.isEmpty()) {
                                    continue;
                                }
                                boolean find = false;
                                if (!itemStackList.isEmpty()) {
                                    for (ItemStack stack : itemStackList) {
                                        if (stack.getItem() == output.getItem()) {
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
                                            if (stack.getItem() == output.getItem()) {
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
                                        if (!(output.getItem() == input.getItem())) {
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
        } else {
            for (Direction facing1 : enumFacings) {
                final HandlerInventory handler = this.iItemHandlerMap.get(facing1);

                if (handler == null) {
                    continue;
                }
                if (handler.getInventory() != null && handler.getInventory() instanceof BlockEntityInventory) {
                    final List<Inventory> inputs = ((BlockEntityInventory) this.base).getInputSlots();
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
                                            if (input.getCount() == input.getMaxStackSize()) {
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
                            final ItemStack took1 = ModUtils.insertItem(
                                    this.main_handler,
                                    took,
                                    true,
                                    this.main_handler.getSlots()
                            );
                            if (took1.isEmpty()) {
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
    }

    private void tick(final int i) {
        Direction facing = this.facings[i];
        List<ItemStack> itemStackList = this.whiteList.get(i);
        if (facing != null) {

            final HandlerInventory handler = this.iItemHandlerMap.get(facing);
            if (handler == null) {
                return;
            }
            int slots = 0;
            if (handler.getInventory() != null && handler.getInventory() instanceof BlockEntityInventory) {
                BlockEntityInventory inventory = (BlockEntityInventory) handler.getInventory();
                cycle:
                for (InventoryOutput slot : this.slots) {
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
                }
            } else {
                try {
                    slots = this.slotHandler.get(handler.getHandler());
                } catch (Exception ignored) {
                }
                if (handler.getInventory() != null) {
                    for (InventoryOutput slot : this.slots) {
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
                    }
                } else {
                    for (InventoryOutput slot : this.slots) {
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
        } else {
            for (Direction facing1 : enumFacings) {
                final HandlerInventory handler = this.iItemHandlerMap.get(facing1);
                if (handler == null) {
                    continue;
                }

                int slots = 0;
                if (handler.getInventory() != null && handler.getInventory() instanceof BlockEntityInventory) {
                    BlockEntityInventory inventory = (BlockEntityInventory) handler.getInventory();
                    cycle:
                    for (InventoryOutput slot : this.slots) {
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
                    try {
                        slots = this.slotHandler.get(handler.getHandler());
                    } catch (Exception ignored) {
                    }
                    if (handler.getInventory() != null) {
                        for (InventoryOutput slot : this.slots) {
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
                                    int count = slot.get(j).getCount() - stack.getCount();
                                    slot.get(j).shrink(count);
                                    stack.setCount(count);
                                    insertItem1(handler, stack, false, slots);
                                }


                            }
                        }

                    } else {
                        for (InventoryOutput slot : this.slots) {
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
                                    slot.get(j).shrink(stack.getCount());
                                    ModUtils.insertItem(handler.getHandler(), stack, false, slots);
                                }

                            }
                        }
                    }
                }
            }

        }
    }

    private void tick_fluid(final int i) {
        Direction facing = this.facings[i];
        final List<Fluid> itemStackList = this.whiteList1.get(i);
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
                if (!itemStackList.isEmpty() && !itemStackList.contains(tank.getFluid().getFluid())) {
                    continue;
                }
                int amount = handler.fill(tank.getFluid(), IFluidHandler.FluidAction.SIMULATE);
                if (amount > 0 && tank.canDrain(facing)) {
                    tank.drain(handler.fill(tank.getFluid(), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        } else {
            for (Direction facing1 : enumFacings) {
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
                    if (!itemStackList.isEmpty() && !itemStackList.contains(tank.getFluid().getFluid())) {
                        continue;
                    }
                    int amount = handler.fill(tank.getFluid(), IFluidHandler.FluidAction.SIMULATE);
                    if (amount > 0 && tank.canDrain(facing1)) {
                        tank.drain(handler.fill(tank.getFluid(), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
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
        final Container inventory = dest1.getInventory();
        ItemStack stackInSlot;
        int maxSlots = dest.getSlots();
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
                // copy the stack to not modify the original one
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
                // copy the stack to not modify the original one
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


    public double getEnergyStorage(int defaultEnergyStorage, int defaultOperationLength, int defaultEnergyDemand) {
        int opLen = this.getOperationLength(defaultOperationLength);
        double energyDemand = this.getEnergyDemand(defaultEnergyDemand);
        return applyModifier(
                defaultEnergyStorage,
                this.extraEnergyStorage + opLen * energyDemand,
                this.energyStorageMultiplier
        );
    }

}
