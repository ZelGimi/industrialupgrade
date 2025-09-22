package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InventoryFluidByList extends InventoryFluid implements ITypeSlot {

    boolean usually = false;
    private Set<Fluid> acceptedFluids;

    public InventoryFluidByList(
            TileEntityInventory base1,
            TypeItemSlot typeItemSlot1,
            int count,
            TypeFluidSlot typeFluidSlot,
            Fluid... fluidlist
    ) {
        super(base1, typeItemSlot1, count, typeFluidSlot);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    public InventoryFluidByList(TileEntityInventory base1, int count, Fluid fluidlist) {
        super(base1, TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(Collections.singletonList(fluidlist));
    }

    public InventoryFluidByList(TileEntityInventory base1, int count, Fluid... fluidlist) {
        super(base1, TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    public InventoryFluidByList(TileEntityInventory base1, int count, List<Fluid> fluidlist) {
        super(base1, TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(fluidlist);
    }

    public InventoryFluidByList(TileEntityInventory base1, String name1, int count, Fluid fluidlist, TypeFluidSlot TypeFluidSlot) {
        super(base1, TypeItemSlot.INPUT, count, TypeFluidSlot);
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
