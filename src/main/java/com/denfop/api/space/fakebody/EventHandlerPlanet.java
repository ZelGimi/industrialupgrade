package com.denfop.api.space.fakebody;


import com.denfop.Constants;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.research.IResearchTable;
import com.denfop.api.space.research.event.ResearchTableLoadEvent;
import com.denfop.events.WorldSavedDataIU;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;
import java.util.Objects;

public class EventHandlerPlanet {

    private final boolean load;
    public WorldSavedDataIU data;

    public EventHandlerPlanet() {
        this.load = false;
    }

    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.world.provider.getWorldTime() % 20 == 0) {
                SpaceNet.instance.getFakeSpaceSystem().working();
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
        final Map<FakePlayer, IResearchTable> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getResearchTableMap();
        if (!map.containsKey(event.table.getPlayer())) {
            map.put(event.table.getPlayer(), event.table);
        }

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
