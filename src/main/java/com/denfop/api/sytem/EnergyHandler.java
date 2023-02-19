package com.denfop.api.sytem;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EnergyHandler {

    public EnergyHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            for (IGlobalNet globalNet : EnergyBase.listGlobal) {
                globalNet.TickEnd(event.world.provider.getDimension());
            }
        }
    }

    @SubscribeEvent
    public void tick(final EnergyEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        IGlobalNet globalNet = EnergyBase.globalNetMap.get(event.getEnergyType());
        if (globalNet != null) {
            if (event.getEvent() == EnumTypeEvent.LOAD) {
                globalNet.addTile(event.getWorld(), event.getTile());
            } else {
                globalNet.removeTile(event.getWorld(), event.getTile());
            }
        }


    }

}
