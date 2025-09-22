package com.denfop.api.tesseract;

import net.minecraft.world.World;

import java.util.List;

public interface ITesseractSystem {

    void addTesseract(ITesseract tesseract);

    void removeTesseract(ITesseract tesseract);

    void addChannel(Channel channel);

    void removeChannel(Channel channel);

    void onTick(World world);

    void onWorldUnload(World world);

    List<Channel> getPublicChannels(World world);

}
