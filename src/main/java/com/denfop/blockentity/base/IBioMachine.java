package com.denfop.blockentity.base;

import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.componets.HeatComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IBioMachine {

    FluidTank getTank();

    HeatComponent getHeat();

    void initiate(int soundEvent);

    EnumTypeAudio getTypeAudio();

}
