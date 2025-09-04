package com.denfop.api.otherenergies.common;

import com.denfop.api.otherenergies.common.event.EnergyEvent;
import com.denfop.api.otherenergies.common.event.EnumTypeEvent;
import com.denfop.api.otherenergies.common.interfaces.GlobalNet;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnergyHandler {

    public EnergyHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void tick(final TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            for (GlobalNet globalNet : EnergyBase.listGlobal) {
                globalNet.TickEnd(event.level.dimension());
            }
        }
    }

    @SubscribeEvent
    public void tick(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        for (GlobalNet globalNet : EnergyBase.listGlobal) {
            globalNet.onUnload(((Level) event.getLevel()).dimension());
        }

    }

    @SubscribeEvent
    public void tick(final EnergyEvent event) {

        GlobalNet globalNet = EnergyBase.globalNetMap.get(event.getEnergyType());
        if (globalNet != null) {
            if (event.getEvent() == EnumTypeEvent.LOAD) {
                globalNet.addTile((Level) event.getLevel(), event.getTile());
            } else {
                globalNet.removeTile((Level) event.getLevel(), event.getTile());
            }
        }


    }

}
