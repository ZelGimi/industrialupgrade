package com.denfop.api.sytem;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EnergyEvent extends WorldEvent {

    private final EnumTypeEvent event;
    private final EnergyType energyType;
    private final ITile tile;

    public EnergyEvent(final World world, EnumTypeEvent event, EnergyType energyType, ITile tile) {
        super(world);
        this.event = event;
        this.energyType = energyType;
        this.tile = tile;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public EnumTypeEvent getEvent() {
        return event;
    }

    public ITile getTile() {
        return tile;
    }


}
