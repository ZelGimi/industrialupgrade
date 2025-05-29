package com.denfop.api.tesseract;


import net.minecraft.world.level.Level;

import java.util.List;

public interface ITesseractSystem {

    void addTesseract(ITesseract tesseract);

    void removeTesseract(ITesseract tesseract);

    void addChannel(Channel channel);

    void removeChannel(Channel channel);

    void onTick(Level world);

    void onWorldUnload(Level world);

    List<Channel> getPublicChannels(Level world);

}
