package com.denfop.api.otherenergies.common.event;


import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.otherenergies.common.interfaces.Tile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class EnergyEvent extends LevelEvent {

    private final EnumTypeEvent event;
    private final EnergyType energyType;
    private final Tile tile;

    public EnergyEvent(final Level world, EnumTypeEvent event, EnergyType energyType, Tile tile) {
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

    public Tile getTile() {
        return tile;
    }


}
