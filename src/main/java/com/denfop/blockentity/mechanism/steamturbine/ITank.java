package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.api.multiblock.MultiBlockElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ITank extends MultiBlockElement {

    FluidTank getTank();

    void setWaterTank();

    void setSteamTank();

    void clear(boolean steam);

}
