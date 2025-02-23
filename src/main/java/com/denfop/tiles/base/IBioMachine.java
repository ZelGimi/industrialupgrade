package com.denfop.tiles.base;

import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.componets.HeatComponent;
import net.minecraftforge.fluids.FluidTank;

public interface IBioMachine {

    FluidTank getTank();

    HeatComponent getHeat();

    void initiate(int soundEvent);

    EnumTypeAudio getType();

}
