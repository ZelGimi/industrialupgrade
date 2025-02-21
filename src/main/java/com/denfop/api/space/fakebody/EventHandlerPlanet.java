package com.denfop.api.space.fakebody;


import com.denfop.Constants;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.research.event.ResearchTableLoadEvent;
import com.denfop.api.space.research.event.ResearchTableUnLoadEvent;
import com.denfop.api.space.research.event.RocketPadLoadEvent;
import com.denfop.api.space.research.event.RocketPadUnLoadEvent;
import com.denfop.events.WorldSavedDataIU;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class EventHandlerPlanet {

    private final boolean load;
    public WorldSavedDataIU data;

    public EventHandlerPlanet() {
        this.load = false;
    }

    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.world.provider.getDimension() == 0 && event.world.provider.getWorldTime() % 20 == 0) {
                SpaceNet.instance.getFakeSpaceSystem().working();
                SpaceNet.instance.getColonieNet().working();
            }
        }


    }

    @SubscribeEvent
    public void onQuit(final WorldEvent.Unload event) {
        if (event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote) {
            if (data != null) {
                event.getWorld().getMapStorage().saveData(data);
            }
        }
    }

    @SubscribeEvent
    public void load(final ResearchTableLoadEvent event) {
        if (event.getWorld().isRemote)
            return;
        final Map<UUID, IResearchTable> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getResearchTableMap();
        if (!map.containsKey(event.table.getPlayer())) {
            map.put(event.table.getPlayer(), event.table);
        }else{
            map.replace(event.table.getPlayer(),event.table);
        }

    }
    @SubscribeEvent
    public void unLoad(final ResearchTableUnLoadEvent event) {
        if (event.getWorld().isRemote)
            return;
        final Map<UUID, IResearchTable> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getResearchTableMap();
        map.remove(event.table.getPlayer());

    }

    @SubscribeEvent
    public void loadRocketPad(final RocketPadLoadEvent event) {
        if (event.getWorld().isRemote)
            return;
        final Map<UUID, IRocketLaunchPad> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getRocketPadMap();
        if (!map.containsKey(event.table.getPlayer())) {
            map.put(event.table.getPlayer(), event.table);
        }else{
            map.replace(event.table.getPlayer(),event.table);
        }

    }
    @SubscribeEvent
    public void unLoadRocketPad(final RocketPadUnLoadEvent event) {
        if (event.getWorld().isRemote)
            return;
        final Map<UUID, IRocketLaunchPad> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getRocketPadMap();
        map.remove(event.table.getPlayer());

    }

    @SubscribeEvent
    public void loadWorld(final WorldEvent.Load event) {
        if (event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote) {

            this.data = (WorldSavedDataIU) Objects.requireNonNull(event.getWorld().getMapStorage())
                    .getOrLoadData(
                            WorldSavedDataIU.class,
                            Constants.MOD_ID
                    );
            if (data == null) {
                data = new WorldSavedDataIU();
                data.setWorld(event.getWorld());
                event.getWorld().getMapStorage().setData(Constants.MOD_ID, data);
            } else {
                data.setWorld(event.getWorld());
            }
            data.markDirty();
        }
    }

}
