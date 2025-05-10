package com.denfop.api.multiblock;

import net.minecraft.world.entity.player.Player;

public interface IMainMultiBlock extends IMultiElement {

    boolean isFull();

    void setFull(boolean full);

    MultiBlockStructure getMultiBlockStucture();

    int getBlockLevel();

    boolean wasActivated();

    void setActivated(boolean active);

    void updateFull();

    void updateFull(Player player);


}
