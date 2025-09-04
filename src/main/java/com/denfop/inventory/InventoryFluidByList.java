package com.denfop.inventory;

import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public class InventoryFluidByList extends InventoryFluid implements ITypeSlot {

    boolean usually = false;
    private Set<Fluid> acceptedFluids;

    public InventoryFluidByList(
            BlockEntityInventory base1,
            Inventory.TypeItemSlot typeItemSlot1,
            int count,
            TypeFluidSlot typeFluidSlot,
            Fluid... fluidlist
    ) {
        super(base1, typeItemSlot1, count, typeFluidSlot);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    public InventoryFluidByList(BlockEntityInventory base1, int count, Fluid fluidlist) {
        super(base1, Inventory.TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(Collections.singletonList(fluidlist));
    }

    public InventoryFluidByList(BlockEntityInventory base1, int count, Fluid... fluidlist) {
        super(base1, Inventory.TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    public InventoryFluidByList(BlockEntityInventory base1, int count, List<Fluid> fluidlist) {
        super(base1, Inventory.TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
        this.acceptedFluids = new HashSet<>(fluidlist);
    }

    public InventoryFluidByList(BlockEntityInventory base1, String name1, int count, Fluid fluidlist, TypeFluidSlot TypeFluidSlot) {
        super(base1, Inventory.TypeItemSlot.INPUT, count, TypeFluidSlot);
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
