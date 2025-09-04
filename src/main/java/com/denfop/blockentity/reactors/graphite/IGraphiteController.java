package com.denfop.blockentity.reactors.graphite;

import com.denfop.api.multiblock.MultiBlockElement;
import net.minecraft.world.item.ItemStack;

public interface IGraphiteController extends MultiBlockElement {

    ItemStack getGraphite();

    int getLevelGraphite();

    double getFuelGraphite();

    void consumeFuelGraphite(double col);

    void consumeGraphite();

    void setIndex(int i);

}
