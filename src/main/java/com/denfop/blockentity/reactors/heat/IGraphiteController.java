package com.denfop.blockentity.reactors.heat;

import com.denfop.api.multiblock.MultiBlockElement;
import net.minecraft.world.item.ItemStack;

public interface IGraphiteController extends MultiBlockElement {

    ItemStack getGraphite();

    int getLevelGraphite();

    double getFuelGraphite();

    void consumeFuelGraphite(double col);

    void consumeGraphite();

    int getIndex();

}
