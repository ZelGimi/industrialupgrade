package com.denfop.api.tesseract;


import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TesseractSystem implements ITesseractSystem {

    public static TesseractSystem instance;
    public static Map<ResourceKey<Level>, TesseractLocalSystem> localSystemMap = new HashMap<>();

    private TesseractSystem() {
        instance = this;
        new EventHandler();
    }

    public static void init() {
        if (instance == null) {
            new TesseractSystem();
        }


    }

    public TesseractLocalSystem getForWorld(final Level world) {
        if (world == null) {
            return null;
        }
        final ResourceKey<Level> id = world.dimension();
        if (!localSystemMap.containsKey(id)) {
            final TesseractLocalSystem local = new TesseractLocalSystem();
            localSystemMap.put(id, local);
            return local;
        }
        return localSystemMap.get(id);
    }

    public void addChannel(Channel channel) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(channel.getTesseract().getWorld());
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.addChannel(channel);
        }
    }

    public void removeChannel(Channel channel) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(channel.getTesseract().getWorld());
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.removeChannel(channel);
        }
    }

    public void addTesseract(ITesseract tesseract) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(tesseract.getWorld());
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.add(tesseract);
        }
    }

    public void removeTesseract(ITesseract tesseract) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(tesseract.getWorld());
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.remove(tesseract);
        }
    }

    public void onTick(Level world) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(world);
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.onTick();
        }
    }

    public void onWorldUnload(Level world) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(world);
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.onUnload();
        }
    }

    @Override
    public List<Channel> getPublicChannels(final Level world) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(world);
        if (tesseractLocalSystem != null) {
            return tesseractLocalSystem.getPublicChannels();
        }
        return Collections.emptyList();
    }

}
