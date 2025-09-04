package com.denfop.api.otherenergies.common;

import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class EnergyHandler {

    public EnergyHandler() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void tick(final LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        for (IGlobalNet globalNet : EnergyBase.listGlobal) {
            globalNet.TickEnd(event.getLevel().dimension());
        }

    }

    @SubscribeEvent
    public void tick(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        for (IGlobalNet globalNet : EnergyBase.listGlobal) {
            globalNet.onUnload(((Level) event.getLevel()).dimension());
        }

    }

    @SubscribeEvent
    public void tick(final EnergyEvent event) {

        IGlobalNet globalNet = EnergyBase.globalNetMap.get(event.getEnergyType());
        if (globalNet != null) {
            if (event.getEvent() == EnumTypeEvent.LOAD) {
                globalNet.addTile((Level) event.getLevel(), event.getTile());
            } else {
                globalNet.removeTile((Level) event.getLevel(), event.getTile());
            }
        }


    }

}
