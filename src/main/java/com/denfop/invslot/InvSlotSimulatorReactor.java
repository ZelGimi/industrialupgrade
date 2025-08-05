package com.denfop.invslot;

import com.denfop.api.inv.VirtualSlot;
import com.denfop.tiles.base.TileEntitySimulatorReactor;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class InvSlotSimulatorReactor extends InvSlot implements VirtualSlot {

    public InvSlotSimulatorReactor(TileEntitySimulatorReactor base, final TypeItemSlot typeItemSlot, final int count) {
        super(base, typeItemSlot, count);
    }


    @Override
    public boolean isFluid() {
        return false;
    }

    @Override
    public List<FluidStack> getFluidStackList() {
        return Collections.emptyList();
    }

    @Override
    public void setFluidList(final List<FluidStack> fluidStackList) {

    }

}
