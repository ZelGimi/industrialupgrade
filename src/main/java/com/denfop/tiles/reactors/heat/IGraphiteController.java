package com.denfop.tiles.reactors.heat;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraft.item.ItemStack;

public interface IGraphiteController extends IMultiElement {

    ItemStack getGraphite();

    int getLevelGraphite();

    double getFuelGraphite();

    void consumeFuelGraphite(double col);

    void consumeGraphite();

    int getIndex();
}
