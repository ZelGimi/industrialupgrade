package com.denfop.api.otherenergies.common;


import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class EnergyEvent extends LevelEvent {

    private final EnumTypeEvent event;
    private final EnergyType energyType;
    private final ITile tile;

    public EnergyEvent(final Level world, EnumTypeEvent event, EnergyType energyType, ITile tile) {
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
