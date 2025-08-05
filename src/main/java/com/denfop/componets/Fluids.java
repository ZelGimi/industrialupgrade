package com.denfop.componets;

import com.denfop.Localization;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class Fluids extends AbstractComponent {

    public static final Fluid LAVA = net.minecraft.world.level.material.Fluids.LAVA;
    public static final Fluid WATER = net.minecraft.world.level.material.Fluids.WATER;
    public static final Fluid EMPTY = net.minecraft.world.level.material.Fluids.EMPTY;
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
        return this.addTank(name, capacity, InvSlot.TypeItemSlot.INPUT, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTankExtract(String name, int capacity) {
        return this.addTank(name, capacity, InvSlot.TypeItemSlot.OUTPUT, Predicates.alwaysTrue());
    }

    public Fluids.InternalFluidTank addTankExtract(String name, int capacity, Predicate<Fluid> acceptedFluids) {
        return this.addTank(name, capacity, InvSlot.TypeItemSlot.OUTPUT, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity) {
        return this.addTank(name, capacity, InvSlot.TypeItemSlot.INPUT_OUTPUT);
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity, InvSlot.TypeItemSlot typeItemSlot) {
        return this.addTank(name, capacity, typeItemSlot, Predicates.alwaysTrue());
    }

    public Fluids.InternalFluidTank addTank(String name, int capacity, Predicate<Fluid> acceptedFluids) {
        return this.addTank(name, capacity, InvSlot.TypeItemSlot.INPUT_OUTPUT, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTank(
            String name, int capacity, Predicate<Fluid> acceptedFluids,
            InvSlot.TypeItemSlot slot
    ) {
        return this.addTank(name, capacity, slot, acceptedFluids);
    }

    public Fluids.InternalFluidTank addTank(
            String name,
            int capacity,
            InvSlot.TypeItemSlot typeItemSlot,
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
            Collection<Direction> inputSides,
            Collection<Direction> outputSides,
            Predicate<Fluid> acceptedFluids, InvSlot.TypeItemSlot typeItemSlot
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

    public void changeConnectivity(Fluids.InternalFluidTank tank, InvSlot.TypeItemSlot typeItemSlot) {
        this.changeConnectivity(
                tank,
                typeItemSlot.isInput() ? ModUtils.allFacings : Collections.emptySet(),
                typeItemSlot.isOutput() ? ModUtils.allFacings : Collections.emptySet()
        );
    }

    public void changeConnectivity(
            Fluids.InternalFluidTank tank,
            Collection<Direction> inputSides,
            Collection<Direction> outputSides
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

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        for (final InternalFluidTank tank : this.managedTanks) {
            CompoundTag subTag = new CompoundTag();
            subTag = tank.writeToNBT(player.registryAccess(), subTag);
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
        CustomPacketBuffer buffer = new CustomPacketBuffer(parent.registryAccess());
        for (final InternalFluidTank tank : this.managedTanks) {
            CompoundTag subTag = new CompoundTag();
            subTag = tank.writeToNBT(parent.registryAccess(), subTag);
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
            tank.readFromNBT(is.registryAccess(), (CompoundTag) DecoderHandler.decode(is));
        }

    }

    public void readFromNbt(CompoundTag nbt) {

        for (final InternalFluidTank tank : this.managedTanks) {
            if (nbt.contains(tank.identifier, 10)) {
                tank.readFromNBT(parent.provider, nbt.getCompound(tank.identifier));
            }
        }

    }

    public CompoundTag writeToNbt() {
        CompoundTag nbt = new CompoundTag();

        for (final InternalFluidTank tank : this.managedTanks) {
            CompoundTag subTag = new CompoundTag();
            subTag = tank.writeToNBT(parent.provider, subTag);
            nbt.put(tank.identifier, subTag);
        }

        return nbt;
    }

    @Override
    public Collection<? extends BlockCapability<?, ?>> getProvidedCapabilities(Direction side) {
        return Collections.singleton(Capabilities.FluidHandler.BLOCK);
    }

    @Override
    public <T> T getCapability(BlockCapability<T, Direction> cap, Direction side) {
        return cap == Capabilities.FluidHandler.BLOCK ? (T) new FluidHandler(side) : super.getCapability(
                cap,
                side)
                ;
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

    public class InternalFluidTank extends FluidTank {

        protected final String identifier;
        List<String> fluidList = new ArrayList<>();
        private InvSlot.TypeItemSlot typeItemSlot;
        private Predicate<Fluid> acceptedFluids;
        private List<Direction> inputSides;
        private List<Direction> outputSides;
        private boolean canAccept = true;

        protected InternalFluidTank(
                String identifier,
                Collection<Direction> inputSides,
                Collection<Direction> outputSides,
                Predicate<Fluid> acceptedFluids,
                int capacity, InvSlot.TypeItemSlot typeItemSlot
        ) {
            super(capacity);
            this.identifier = identifier;
            this.acceptedFluids = acceptedFluids;
            this.inputSides = new ArrayList<>(inputSides);
            this.outputSides = new ArrayList<>(outputSides);
            this.typeItemSlot = typeItemSlot;
            List<Fluid> fluidList1 = BuiltInRegistries.FLUID.stream().filter(acceptedFluids).toList();
            for (Fluid fluid1 : fluidList1) {
                fluidList.add(Localization.translate(fluid1.getFluidType().getDescriptionId()));
            }

        }

        public CustomPacketBuffer writePacket() {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer(parent.registryAccess());
            try {
                EncoderHandler.encode(packetBuffer, fluid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return packetBuffer;
        }

        public void readPacket(CustomPacketBuffer packetBuffer) {
            try {
                this.fluid = (FluidStack) DecoderHandler.decode(packetBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public List<String> getFluidList() {
            return fluidList;
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
        }

        public void setTypeItemSlot(final InvSlot.TypeItemSlot typeItemSlot) {
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


        public boolean isFluidValid(FluidStack stack) {
            return this.acceptsFluid(stack.getFluid());
        }


        public boolean acceptsFluid(Fluid fluid) {
            return this.acceptedFluids.apply(fluid);
        }

        public Predicate<Fluid> getAcceptedFluids() {
            return acceptedFluids;
        }

        public void setAcceptedFluids(final Predicate<Fluid> acceptedFluids) {
            this.acceptedFluids = acceptedFluids;
            List<Fluid> fluidList1 = BuiltInRegistries.FLUID.stream().filter(acceptedFluids).toList();
            fluidList.clear();
            for (Fluid fluid1 : fluidList1) {
                fluidList.add(Localization.translate(fluid1.getFluidType().getDescriptionId()));
            }

        }

        public boolean canFill(Direction side) {
            return canAccept && this.inputSides.contains(side);
        }

        public boolean canDrain(Direction side) {
            return this.outputSides.contains(side);
        }

        public void setCanAccept(boolean b) {
            this.canAccept = b;
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    private class FluidHandler implements IFluidHandler {

        private final Direction side;

        FluidHandler(Direction side) {
            this.side = side;
        }


        @Override
        public int getTanks() {
            return Fluids.this.managedTanks.size();
        }


        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return Fluids.this.managedTanks.get(tank).getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return Fluids.this.managedTanks.get(tank).getTankCapacity(0);
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return Fluids.this.managedTanks.get(tank).acceptsFluid(stack.getFluid());
        }


        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource != null && resource.getAmount() > 0) {
                int total = 0;
                FluidStack missing = resource.copy();
                for (final InternalFluidTank tank : Fluids.this.getAllTanks()) {
                    if (tank.canFill(this.side)) {
                        total += tank.fill(missing, action);
                        missing.setAmount(resource.getAmount() - total);
                        if (missing.getAmount() <= 0) {
                            break;
                        }
                    }
                }

                return total;
            } else {
                return 0;
            }
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource != null && resource.getAmount() > 0) {
                FluidStack ret = new FluidStack(resource.getFluid(), 0);

                for (final InternalFluidTank tank : Fluids.this.getAllTanks()) {
                    if (tank.canDrain(this.side)) {
                        FluidStack inTank = tank.getFluid();
                        if (!inTank.isEmpty() && inTank.getFluid() == resource.getFluid()) {
                            FluidStack add = tank.drain(resource.getAmount() - ret.getAmount(), action);
                            if (!add.isEmpty()) {
                                assert add.getFluid() == resource.getFluid();

                                ret.setAmount(ret.getAmount() + add.getAmount());
                                if (ret.getAmount() >= resource.getAmount()) {
                                    break;
                                }
                            }
                        }
                    }
                }

                return ret.getAmount() == 0 ? FluidStack.EMPTY : ret;
            } else {
                return FluidStack.EMPTY;
            }
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            Iterable<InternalFluidTank> tanks = Fluids.this.getAllTanks();
            for (final InternalFluidTank tank : tanks) {
                if (tank.canDrain(this.side)) {
                    FluidStack stack = tank.drain(maxDrain, action);
                    if (!stack.isEmpty()) {
                        stack.setAmount(maxDrain);
                        return this.drain(stack, action);
                    }
                }
            }

            return FluidStack.EMPTY;
        }
    }


}
