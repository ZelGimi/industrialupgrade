package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public class InvSlotFluidByList extends InvSlotFluid implements ITypeSlot {

    boolean usually = false;
    private Set<Fluid> acceptedFluids;

    public InvSlotFluidByList(
            TileEntityInventory base1,
            InvSlot.TypeItemSlot typeItemSlot1,
            int count,
            TypeFluidSlot typeFluidSlot,
            Fluid... fluidlist
    ) {
        super(base1, typeItemSlot1, count, typeFluidSlot);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    public InvSlotFluidByList(TileEntityInventory base1, int count, Fluid fluidlist) {
        super(base1, InvSlot.TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(Collections.singletonList(fluidlist));
    }

    public InvSlotFluidByList(TileEntityInventory base1, int count, Fluid... fluidlist) {
        super(base1, InvSlot.TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    public InvSlotFluidByList(TileEntityInventory base1, int count, List<Fluid> fluidlist) {
        super(base1, InvSlot.TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(fluidlist);
    }

    public InvSlotFluidByList(TileEntityInventory base1, String name1, int count, Fluid fluidlist, TypeFluidSlot TypeFluidSlot) {
        super(base1, InvSlot.TypeItemSlot.INPUT, count, TypeFluidSlot);
        this.acceptedFluids = new HashSet<>(Collections.singletonList(fluidlist));
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.BUCKET;
    }

    public void setUsually(final boolean usually) {
        this.usually = usually;
    }

    public Set<Fluid> getAcceptedFluids() {
        return acceptedFluids;
    }

    public void setAcceptedFluids(final Set<Fluid> acceptedFluids) {
        this.acceptedFluids = acceptedFluids;
    }

    protected boolean acceptsLiquid(Fluid fluid) {
        return this.usually || this.acceptedFluids.contains(fluid);
    }

    protected Iterable<Fluid> getPossibleFluids() {
        return this.acceptedFluids;
    }

}
