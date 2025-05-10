package com.denfop.api.space.fakebody;


import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.research.event.ResearchTableLoadEvent;
import com.denfop.api.space.research.event.ResearchTableUnLoadEvent;
import com.denfop.api.space.research.event.RocketPadLoadEvent;
import com.denfop.api.space.research.event.RocketPadUnLoadEvent;
import com.denfop.events.WorldSavedDataIU;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

public class EventHandlerPlanet {

    public static WorldSavedDataIU data;
    private final boolean load;
    int tick = 0;

    public EventHandlerPlanet() {
        this.load = false;
    }

    @SubscribeEvent
    public void tick(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tick++;
            if (tick % 20 == 0) {
                SpaceNet.instance.getFakeSpaceSystem().working();
                SpaceNet.instance.getColonieNet().working();
            }
        }


    }


    @SubscribeEvent
    public void load(final ResearchTableLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final Map<UUID, IResearchTable> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getResearchTableMap();
        if (!map.containsKey(event.table.getPlayer())) {
            map.put(event.table.getPlayer(), event.table);
        }

    }

    @SubscribeEvent
    public void unLoad(final ResearchTableUnLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final Map<UUID, IResearchTable> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getResearchTableMap();
        map.remove(event.table.getPlayer());

    }

    @SubscribeEvent
    public void loadRocketPad(final RocketPadLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final Map<UUID, IRocketLaunchPad> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getRocketPadMap();
        if (!map.containsKey(event.table.getPlayer())) {
            map.put(event.table.getPlayer(), event.table);
        }

    }

    @SubscribeEvent
    public void unLoadRocketPad(final RocketPadUnLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final Map<UUID, IRocketLaunchPad> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getRocketPadMap();
        map.remove(event.table.getPlayer());

    }

    @SubscribeEvent
    public void onSave(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel serverLevel
                && serverLevel.dimension() == Level.OVERWORLD
                && data != null) {
            data.setDirty(true);
        }
    }

    @SubscribeEvent
    public void onUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel
                && serverLevel.dimension() == Level.OVERWORLD
                && data != null) {
            data.setDirty(true);
        }
    }


}
