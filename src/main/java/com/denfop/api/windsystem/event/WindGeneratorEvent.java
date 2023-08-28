package com.denfop.api.windsystem.event;

import com.denfop.api.windsystem.IWindMechanism;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WindGeneratorEvent extends WorldEvent {

    private final IWindMechanism windMechanism;
    private final boolean load;

    public WindGeneratorEvent(IWindMechanism windMechanism, final World world, boolean load) {
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
