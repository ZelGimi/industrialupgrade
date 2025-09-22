package com.denfop.componets;

import com.denfop.Localization;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Fluids extends AbstractComponent {

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


    public Fluids.InternalFluidTank addTankInsert(String name, int capacity) {
        return this.addTankInsert(name, capacity, Predicates.alwaysTrue());
    }


    public Fluids.InternalFluidTank addTankInsert(
            String name,
            int capacity,
            Predicate<Fluid> acceptedFluids
    ) {
        return this.addTank(name, capacity, Inventory.TypeItemSlot.INPUT, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTankExtract(String name, int capacity) {
        return this.addTank(name, capacity, Inventory.TypeItemSlot.OUTPUT, Predicates.alwaysTrue());
    }

    public Fluids.InternalFluidTank addTankExtract(String name, int capacity, Predicate<Fluid> acceptedFluids) {
        return this.addTank(name, capacity, Inventory.TypeItemSlot.OUTPUT, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity) {
        return this.addTank(name, capacity, Inventory.TypeItemSlot.INPUT_OUTPUT);
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity, Inventory.TypeItemSlot typeItemSlot) {
        return this.addTank(name, capacity, typeItemSlot, Predicates.alwaysTrue());
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity, Predicate<Fluid> acceptedFluids) {
        return this.addTank(name, capacity, Inventory.TypeItemSlot.INPUT_OUTPUT, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTank(
            String name, int capacity, Predicate<Fluid> acceptedFluids,
            Inventory.TypeItemSlot slot
    ) {
        return this.addTank(name, capacity, slot, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTank(
            String name,
            int capacity,
            Inventory.TypeItemSlot typeItemSlot,
            Predicate<Fluid> acceptedFluids
    ) {
        return this.addTank(name, capacity,
                typeItemSlot.isInput() ? ModUtils.allFacings : Collections.emptySet(),
                typeItemSlot.isOutput() ? ModUtils.allFacings : Collections.emptySet(), acceptedFluids, typeItemSlot
        );
    }

    public Fluids.InternalFluidTank addTank(
            String name,
            int capacity,
            Collection<EnumFacing> inputSides,
            Collection<EnumFacing> outputSides,
            Predicate<Fluid> acceptedFluids, Inventory.TypeItemSlot typeItemSlot
    ) {
        return this.addTank(new Fluids.InternalFluidTank(name, inputSides, outputSides, acceptedFluids, capacity, typeItemSlot));
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

    public void changeConnectivity(Fluids.InternalFluidTank tank, Inventory.TypeItemSlot typeItemSlot) {
        this.changeConnectivity(
                tank,
                typeItemSlot.isInput() ? ModUtils.allFacings : Collections.emptySet(),
                typeItemSlot.isOutput() ? ModUtils.allFacings : Collections.emptySet()
        );
    }

    public void changeConnectivity(
            Fluids.InternalFluidTank tank,
            Collection<EnumFacing> inputSides,
            Collection<EnumFacing> outputSides
    ) {
        assert this.managedTanks.contains(tank);

        tank.inputSides = new ArrayList<>(inputSides);
        tank.outputSides = new ArrayList<>(outputSides);
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

    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        for (final InternalFluidTank tank : this.managedTanks) {
            NBTTagCompound subTag = new NBTTagCompound();
            subTag = tank.writeToNBT(subTag);
            try {
                EncoderHandler.encode(buffer, subTag);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.setNetworkUpdate(player, buffer);
    }

    @Override
    public CustomPacketBuffer updateComponent() {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        for (final InternalFluidTank tank : this.managedTanks) {
            NBTTagCompound subTag = new NBTTagCompound();
            subTag = tank.writeToNBT(subTag);
            try {
                EncoderHandler.encode(buffer, subTag);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        for (final InternalFluidTank tank : this.managedTanks) {
            tank.readFromNBT((NBTTagCompound) DecoderHandler.decode(is));
        }

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

    public List<InternalFluidTank> getManagedTanks() {
        return managedTanks;
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
        List<String> fluidList = new ArrayList<>();
        private Inventory.TypeItemSlot typeItemSlot;
        private Predicate<Fluid> acceptedFluids;
        private List<EnumFacing> inputSides;
        private List<EnumFacing> outputSides;
        private boolean canAccept = true;

        protected InternalFluidTank(
                String identifier,
                Collection<EnumFacing> inputSides,
                Collection<EnumFacing> outputSides,
                Predicate<Fluid> acceptedFluids,
                int capacity, Inventory.TypeItemSlot typeItemSlot
        ) {
            super(capacity);
            this.identifier = identifier;
            this.acceptedFluids = acceptedFluids;
            this.inputSides = new ArrayList<>(inputSides);
            this.outputSides = new ArrayList<>(outputSides);
            this.typeItemSlot = typeItemSlot;
            List<Fluid> fluidList1 = FluidRegistry.getRegisteredFluids().values().stream().filter(acceptedFluids).collect(
                    Collectors.toList());
            for (Fluid fluid1 : fluidList1) {
                fluidList.add(Localization.translate(fluid1.getUnlocalizedName()));
            }

        }

        public CustomPacketBuffer writePacket() {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
            packetBuffer.writeBoolean(fluid != null);
            if (fluid != null) {
                packetBuffer.writeString(fluid.getFluid().getName());
                packetBuffer.writeInt(fluid.amount);
            }
            return packetBuffer;
        }

        public void readPacket(CustomPacketBuffer packetBuffer) {
            boolean hasFluid = packetBuffer.readBoolean();
            if (hasFluid) {
                String name = packetBuffer.readString();
                int amount = packetBuffer.readInt();
                Fluid fluid = FluidRegistry.getFluid(name);
                this.fluid = new FluidStack(fluid, amount);
            } else {
                fluid = null;
            }
        }

        public List<String> getFluidList() {
            return fluidList;
        }

        public void setTypeItemSlot(final Inventory.TypeItemSlot typeItemSlot) {
            this.typeItemSlot = typeItemSlot;
            if (inputSides != null && outputSides != null) {
                inputSides.clear();
                outputSides.clear();
                this.inputSides.addAll(typeItemSlot.isInput() ? ModUtils.allFacings : Collections.emptySet());
                this.outputSides.addAll(typeItemSlot.isOutput() ? ModUtils.allFacings : Collections.emptySet());
            }
        }

        public boolean isInput() {
            return this.typeItemSlot.isInput();
        }

        public boolean isOutput() {
            return this.typeItemSlot.isOutput();
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

        public Predicate<Fluid> getAcceptedFluids() {
            return acceptedFluids;
        }

        public void setAcceptedFluids(final Predicate<Fluid> acceptedFluids) {
            this.acceptedFluids = acceptedFluids;
            List<Fluid> fluidList1 = FluidRegistry.getRegisteredFluids().values().stream().filter(acceptedFluids).collect(
                    Collectors.toList());
            fluidList.clear();
            for (Fluid fluid1 : fluidList1) {
                fluidList.add(Localization.translate(fluid1.getUnlocalizedName()));
            }

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
            return canAccept && this.inputSides.contains(side);
        }

        public boolean canDrain(EnumFacing side) {
            return this.outputSides.contains(side);
        }

        public void setCanAccept(boolean b) {
            this.canAccept = b;
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
