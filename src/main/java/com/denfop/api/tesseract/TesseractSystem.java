package com.denfop.api.tesseract;

import net.minecraft.world.World;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TesseractSystem implements ITesseractSystem {

    public static TesseractSystem instance;
    public static Map<Integer, TesseractLocalSystem> localSystemMap = new HashMap<>();

    private TesseractSystem() {
        instance = this;
        new EventHandler();
    }

    public static void init() {
        if (instance == null) {
            new TesseractSystem();
        }


    }

    public TesseractLocalSystem getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        final int id = world.provider.getDimension();
        if (!localSystemMap.containsKey(id)) {
            final TesseractLocalSystem local = new TesseractLocalSystem();
            localSystemMap.put(id, local);
            return local;
        }
        return localSystemMap.get(id);
    }

    public void addChannel(Channel channel) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(channel.getTesseract().getLevel());
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.addChannel(channel);
        }
    }

    public void removeChannel(Channel channel) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(channel.getTesseract().getLevel());
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.removeChannel(channel);
        }
    }

    public void addTesseract(ITesseract tesseract) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(tesseract.getLevel());
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.add(tesseract);
        }
    }

    public void removeTesseract(ITesseract tesseract) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(tesseract.getLevel());
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.remove(tesseract);
        }
    }

    public void onTick(World world) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(world);
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.onTick();
        }
    }

    public void onWorldUnload(World world) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(world);
        if (tesseractLocalSystem != null) {
            tesseractLocalSystem.onUnload();
        }
    }

    @Override
    public List<Channel> getPublicChannels(final World world) {
        TesseractLocalSystem tesseractLocalSystem = getForWorld(world);
        if (tesseractLocalSystem != null) {
            return tesseractLocalSystem.getPublicChannels();
        }
        return Collections.emptyList();
    }

}
