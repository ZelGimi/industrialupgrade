package com.denfop.tiles.mechanism.multimechanism;

import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.componets.HeatComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IMultiMachine {

    FluidTank getTank();

    int getSize(int size);

    void consume(int size);

    HeatComponent getHeat();

    boolean canoperate(int size);

    void initiate(int soundEvent);

    EnumTypeAudio getTypeAudio();
}
