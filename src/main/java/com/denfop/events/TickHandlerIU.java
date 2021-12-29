package com.denfop.events;

import com.denfop.IUCore;
import ic2.core.IC2;
import ic2.core.IWorldTickCallback;
import ic2.core.WorldData;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;

public class TickHandlerIU {

    private static final boolean debugupdate = System.getProperty("ic2.debugupdate") != null;
    private static final Map<IWorldTickCallback, Throwable> debugTraces;
    private static Throwable lastDebugTrace;

    static {
        debugTraces = debugupdate ? new WeakHashMap() : null;
    }

    private static void processUpdates(World world, WorldData worldData) {
        IC2.platform.profilerStartSection("single-update");

        IWorldTickCallback callback;
        Queue<IWorldTickCallback> singleUpdates = ReflectionHelper.getPrivateValue(
                WorldData.class,
                worldData,
                new String[]{"singleUpdates"}
        );

        for (; (callback = singleUpdates.poll()) != null; callback.onTick(world)) {
            if (debugupdate) {
                lastDebugTrace = debugTraces.remove(callback);
            }
        }

        IC2.platform.profilerEndStartSection("cont-update");
        ReflectionHelper.setPrivateValue(WorldData.class, worldData, true, "continuousUpdatesInUse");


        IWorldTickCallback update;
        Set<IWorldTickCallback> continuousUpdates = ReflectionHelper.getPrivateValue(
                WorldData.class,
                worldData,
                new String[]{"continuousUpdates"}
        );
        for (Iterator var3 = continuousUpdates.iterator(); var3.hasNext(); update.onTick(world)) {
            update = (IWorldTickCallback) var3.next();
            if (debugupdate) {
                lastDebugTrace = debugTraces.remove(update);
            }
        }
        ReflectionHelper.setPrivateValue(WorldData.class, worldData, false, "continuousUpdatesInUse");


        if (debugupdate) {
            lastDebugTrace = null;
        }
        List<IWorldTickCallback> continuousUpdatesToAdd = ReflectionHelper.getPrivateValue(WorldData.class, worldData,
                new String[]{"continuousUpdatesToAdd"}
        );
        continuousUpdates.addAll(continuousUpdatesToAdd);
        continuousUpdatesToAdd.clear();
        List<IWorldTickCallback> continuousUpdatesToRemove = ReflectionHelper.getPrivateValue(WorldData.class, worldData,
                new String[]{"continuousUpdatesToRemove"}
        );
        continuousUpdates.removeAll(continuousUpdatesToRemove);
        continuousUpdatesToRemove.clear();
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
