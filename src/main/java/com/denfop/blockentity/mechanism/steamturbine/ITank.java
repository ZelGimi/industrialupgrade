package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ITank extends IMultiElement {

    FluidTank getTank();

    void setWaterTank();

    void setSteamTank();

    void clear(boolean steam);

}
