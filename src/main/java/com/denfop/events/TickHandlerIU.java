package com.denfop.events;

import com.denfop.IUCore;
import com.denfop.network.WorldData;
import ic2.core.IC2;
import ic2.core.IWorldTickCallback;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;

public class TickHandlerIU {

    private static void processUpdates(World world, WorldData worldData) {
        IC2.platform.profilerStartSection("single-update");

        IWorldTickCallback callback;
        for (; (callback = worldData.singleUpdates.poll()) != null; callback.onTick(world)) {

        }

        IC2.platform.profilerEndStartSection("cont-update");
        worldData.continuousUpdatesInUse = true;

        IWorldTickCallback update;
        for (Iterator var3 = worldData.continuousUpdates.iterator(); var3.hasNext(); update.onTick(world)) {
            update = (IWorldTickCallback) var3.next();

        }

        worldData.continuousUpdatesInUse = false;


        worldData.continuousUpdates.addAll(worldData.continuousUpdatesToAdd);
        worldData.continuousUpdatesToAdd.clear();
        worldData.continuousUpdates.removeAll(worldData.continuousUpdatesToRemove);
        worldData.continuousUpdatesToRemove.clear();
        IC2.platform.profilerEndSection();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            IUCore.proxy.profilerStartSection("Keyboard");
            IUCore.keyboard.sendKeyUpdate();
            IUCore.proxy.profilerEndStartSection("AudioManager");
            IUCore.audioManager.onTick();
            IUCore.proxy.profilerEndStartSection("updates");


            World world = IC2.platform.getPlayerWorld();
            if (world != null) {
                processUpdates(world, WorldData.get(world));
            }

            IUCore.proxy.profilerEndSection();
        }

    }

}
