package com.denfop.tiles.reactors.water;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.Fluid;

public interface IOutput extends IMultiElement {

    void addFluids(Fluids fluids);

    void clearList();

}
