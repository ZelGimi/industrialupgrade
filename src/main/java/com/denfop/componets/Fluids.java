package com.denfop.componets;

import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import ic2.api.recipe.ILiquidAcceptManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Fluids extends TileEntityAdvComponent {

    protected final List<Fluids.InternalFluidTank> managedTanks = new ArrayList<>();
    protected final List<Supplier<? extends Collection<Fluids.InternalFluidTank>>> unmanagedTanks = new ArrayList();

    public Fluids(TileEntityInventory parent) {
        super(parent);
    }

    public static Predicate<Fluid> fluidPredicate(Fluid... fluids) {
        final Collection<Fluid> acceptedFluids;
        if (fluids.length > 10) {
            acceptedFluids = new HashSet<>(Arrays.asList(fluids));
        } else {
            acceptedFluids = Arrays.asList(fluids);
        }

        return acceptedFluids::contains;
    }

    public static Predicate<Fluid> fluidPredicate(List<Fluid> fluids) {
        final Collection<Fluid> acceptedFluids;
        if (fluids != null) {
            if (fluids.size() > 10) {
                acceptedFluids = new HashSet<>(fluids);
            } else {
                acceptedFluids = fluids;
            }
        } else {
            acceptedFluids = new ArrayList<>();
        }

        return acceptedFluids::contains;
    }

    public static Predicate<Fluid> fluidPredicate(final ILiquidAcceptManager manager) {
        return manager::acceptsFluid;
    }

    public Fluids.InternalFluidTank addTankInsert(String name, int capacity) {
        return this.addTankInsert(name, capacity, Predicates.alwaysTrue());
    }

    public Fluids.InternalFluidTank addTankInsert(String name, int capacity, Predicate<Fluid> acceptedFluids) {
        return this.addTankInsert(name, capacity, InvSlot.InvSide.ANY, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTankInsert(String name, int capacity, InvSlot.InvSide side) {
        return this.addTankInsert(name, capacity, side, Predicates.alwaysTrue());
    }

    public Fluids.InternalFluidTank addTankInsert(
            String name,
            int capacity,
            InvSlot.InvSide side,
            Predicate<Fluid> acceptedFluids
    ) {
        return this.addTank(name, capacity, InvSlot.Access.I, side, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTankExtract(String name, int capacity) {
        return this.addTankExtract(name, capacity, InvSlot.InvSide.ANY);
    }

    public Fluids.InternalFluidTank addTankExtract(String name, int capacity, InvSlot.InvSide side) {
        return this.addTank(name, capacity, InvSlot.Access.O, side);
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity) {
        return this.addTank(name, capacity, InvSlot.Access.IO);
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity, InvSlot.Access access) {
        return this.addTank(name, capacity, access, InvSlot.InvSide.ANY);
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity, Predicate<Fluid> acceptedFluids) {
        return this.addTank(name, capacity, InvSlot.Access.IO, InvSlot.InvSide.ANY, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity, InvSlot.Access access, InvSlot.InvSide side) {
        return this.addTank(name, capacity, access, side, Predicates.alwaysTrue());
    }

    public Fluids.InternalFluidTank addTank(
            String name,
            int capacity,
            InvSlot.Access access,
            InvSlot.InvSide side,
            Predicate<Fluid> acceptedFluids
    ) {
        return this.addTank(name, capacity,
                access.isInput() ? side.getAcceptedSides() : Collections.emptySet(),
                access.isOutput() ? side.getAcceptedSides() : Collections.emptySet(), acceptedFluids
        );
    }

    public Fluids.InternalFluidTank addTank(
            String name,
            int capacity,
            Collection<EnumFacing> inputSides,
            Collection<EnumFacing> outputSides,
            Predicate<Fluid> acceptedFluids
    ) {
        return this.addTank(new Fluids.InternalFluidTank(name, inputSides, outputSides, acceptedFluids, capacity));
    }

    public Fluids.InternalFluidTank addTank(Fluids.InternalFluidTank tank) {
        this.managedTanks.add(tank);
        return tank;
    }

    public void addUnmanagedTanks(Fluids.InternalFluidTank tank) {
        this.unmanagedTanks.add(Suppliers.ofInstance(Collections.singleton(tank)));
    }

    public void addUnmanagedTanks(Collection<Fluids.InternalFluidTank> tanks) {
        this.addUnmanagedTankHook(Suppliers.ofInstance(tanks));
    }

    public void addUnmanagedTankHook(Supplier<? extends Collection<Fluids.InternalFluidTank>> suppl) {
        this.unmanagedTanks.add(suppl);
    }

    public void changeConnectivity(Fluids.InternalFluidTank tank, InvSlot.Access access, InvSlot.InvSide side) {
        this.changeConnectivity(
                tank,
                access.isInput() ? side.getAcceptedSides() : Collections.emptySet(),
                access.isOutput() ? side.getAcceptedSides() : Collections.emptySet()
        );
    }

    public void changeConnectivity(
            Fluids.InternalFluidTank tank,
            Collection<EnumFacing> inputSides,
            Collection<EnumFacing> outputSides
    ) {
        assert this.managedTanks.contains(tank);

        tank.inputSides = inputSides;
        tank.outputSides = outputSides;
    }

    public FluidTank getFluidTank(String name) {
        Iterator<InternalFluidTank> var2 = this.getAllTanks().iterator();

        Fluids.InternalFluidTank tank;
        do {
            if (!var2.hasNext()) {
                throw new IllegalArgumentException("Unable to find tank: " + name);
            }

            tank = var2.next();
        } while (!tank.identifier.equals(name));

        return tank;
    }

    public void readFromNbt(NBTTagCompound nbt) {

        for (final InternalFluidTank tank : this.managedTanks) {
            if (nbt.hasKey(tank.identifier, 10)) {
                tank.readFromNBT(nbt.getCompoundTag(tank.identifier));
            }
        }

    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbt = new NBTTagCompound();

        for (final InternalFluidTank tank : this.managedTanks) {
            NBTTagCompound subTag = new NBTTagCompound();
            subTag = tank.writeToNBT(subTag);
            nbt.setTag(tank.identifier, subTag);
        }

        return nbt;
    }

    public Collection<? extends Capability<?>> getProvidedCapabilities(EnumFacing side) {
        return Collections.singleton(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
    }

    public <T> T getCapability(Capability<T> cap, EnumFacing side) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) new FluidHandler(side) : super.getCapability(
                cap,
                side
        );
    }

    public Iterable<Fluids.InternalFluidTank> getAllTanks() {
        if (this.unmanagedTanks.isEmpty()) {
            return this.managedTanks;
        } else {
            List<Fluids.InternalFluidTank> tanks = new ArrayList<>();
            tanks.addAll(this.managedTanks);

            for (final Supplier<? extends Collection<InternalFluidTank>> unmanagedTank : this.unmanagedTanks) {
                tanks.addAll(unmanagedTank.get());
            }

            return tanks;
        }
    }

    public static class InternalFluidTank extends FluidTank {

        protected final String identifier;
        private Predicate<Fluid> acceptedFluids;
        private Collection<EnumFacing> inputSides;
        private Collection<EnumFacing> outputSides;

        protected InternalFluidTank(
                String identifier,
                Collection<EnumFacing> inputSides,
                Collection<EnumFacing> outputSides,
                Predicate<Fluid> acceptedFluids,
                int capacity
        ) {
            super(capacity);
            this.identifier = identifier;
            this.acceptedFluids = acceptedFluids;
            this.inputSides = inputSides;
            this.outputSides = outputSides;
        }

        public void setAcceptedFluids(final Predicate<Fluid> acceptedFluids) {
            this.acceptedFluids = acceptedFluids;
        }

        public boolean canFillFluidType(FluidStack fluid) {
            return fluid != null && this.acceptsFluid(fluid.getFluid());
        }

        public boolean canDrainFluidType(FluidStack fluid) {
            return fluid != null && this.acceptsFluid(fluid.getFluid());
        }

        public boolean acceptsFluid(Fluid fluid) {
            return this.acceptedFluids.apply(fluid);
        }

        IFluidTankProperties getTankProperties(final EnumFacing side) {
            assert side == null || this.inputSides.contains(side) || this.outputSides.contains(side);

            return new IFluidTankProperties() {
                public FluidStack getContents() {
                    return InternalFluidTank.this.getFluid();
                }

                public int getCapacity() {
                    return InternalFluidTank.this.capacity;
                }

                public boolean canFillFluidType(FluidStack fluidStack) {
                    if (fluidStack != null && fluidStack.amount > 0) {
                        return InternalFluidTank.this.acceptsFluid(fluidStack.getFluid()) && (side == null || InternalFluidTank.this.canFill(
                                side));
                    } else {
                        return false;
                    }
                }

                public boolean canFill() {
                    return InternalFluidTank.this.canFill(side);
                }

                public boolean canDrainFluidType(FluidStack fluidStack) {
                    if (fluidStack != null && fluidStack.amount > 0) {
                        return InternalFluidTank.this.acceptsFluid(fluidStack.getFluid()) && (side == null || InternalFluidTank.this.canDrain(
                                side));
                    } else {
                        return false;
                    }
                }

                public boolean canDrain() {
                    return InternalFluidTank.this.canDrain(side);
                }
            };
        }

        public boolean canFill(EnumFacing side) {
            return this.inputSides.contains(side);
        }

        public boolean canDrain(EnumFacing side) {
            return this.outputSides.contains(side);
        }

    }

    private class FluidHandler implements IFluidHandler {

        private final EnumFacing side;

        FluidHandler(EnumFacing side) {
            this.side = side;
        }

        public IFluidTankProperties[] getTankProperties() {
            List<IFluidTankProperties> props = new ArrayList<>(Fluids.this.managedTanks.size());
            final Iterator<InternalFluidTank> var2 = Fluids.this.getAllTanks().iterator();

            while (true) {
                Fluids.InternalFluidTank tank;
                do {
                    if (!var2.hasNext()) {
                        return props.toArray(new IFluidTankProperties[0]);
                    }

                    tank = var2.next();
                } while (!tank.canFill(this.side) && !tank.canDrain(this.side));

                props.add(tank.getTankProperties(this.side));
            }
        }

        public int fill(FluidStack resource, boolean doFill) {
            if (resource != null && resource.amount > 0) {
                int total = 0;
                FluidStack missing = resource.copy();

                for (final InternalFluidTank tank : Fluids.this.getAllTanks()) {
                    if (tank.canFill(this.side)) {
                        total += tank.fill(missing, doFill);
                        missing.amount = resource.amount - total;
                        if (missing.amount <= 0) {
                            break;
                        }
                    }
                }

                return total;
            } else {
                return 0;
            }
        }

        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (resource != null && resource.amount > 0) {
                FluidStack ret = new FluidStack(resource.getFluid(), 0);

                for (final InternalFluidTank tank : Fluids.this.getAllTanks()) {
                    if (tank.canDrain(this.side)) {
                        FluidStack inTank = tank.getFluid();
                        if (inTank != null && inTank.getFluid() == resource.getFluid()) {
                            FluidStack add = tank.drain(resource.amount - ret.amount, doDrain);
                            if (add != null) {
                                assert add.getFluid() == resource.getFluid();

                                ret.amount += add.amount;
                                if (ret.amount >= resource.amount) {
                                    break;
                                }
                            }
                        }
                    }
                }

                return ret.amount == 0 ? null : ret;
            } else {
                return null;
            }
        }

        public FluidStack drain(int maxDrain, boolean doDrain) {

            for (final InternalFluidTank tank : Fluids.this.getAllTanks()) {
                if (tank.canDrain(this.side)) {
                    FluidStack stack = tank.drain(maxDrain, false);
                    if (stack != null) {
                        stack.amount = maxDrain;
                        return this.drain(stack, doDrain);
                    }
                }
            }

            return null;
        }

    }

}
