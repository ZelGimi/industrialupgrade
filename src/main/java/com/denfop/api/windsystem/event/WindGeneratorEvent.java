package com.denfop.api.windsystem.event;

import com.denfop.api.windsystem.IWindMechanism;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class WindGeneratorEvent extends LevelEvent {

    private final IWindMechanism windMechanism;
    private final boolean load;

    public WindGeneratorEvent(IWindMechanism windMechanism, final Level world, boolean load) {
        super(world);
        this.windMechanism = windMechanism;
        this.load = load;
    }

    public IWindMechanism getWindMechanism() {
        return windMechanism;
    }

    public boolean getLoad() {
        return this.load;
    }

}
