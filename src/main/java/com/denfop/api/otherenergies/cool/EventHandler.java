package com.denfop.api.otherenergies.cool;


import com.denfop.api.otherenergies.cool.event.CoolTileLoadEvent;
import com.denfop.api.otherenergies.cool.event.CoolTileUnloadEvent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final CoolTileLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final CoolNetLocal local = CoolNetGlobal.getForWorld((Level) event.getLevel());

        if (local != null) {
            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final CoolTileUnloadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final CoolNetLocal local = CoolNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }

    @SubscribeEvent
    public void tick(final TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            CoolNetGlobal.onTickEnd(event.level);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        CoolNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
