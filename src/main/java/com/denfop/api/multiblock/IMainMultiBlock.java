package com.denfop.api.multiblock;

import net.minecraft.entity.player.EntityPlayer;

public interface IMainMultiBlock extends IMultiElement {

    boolean isFull();

    void setFull(boolean full);

    MultiBlockStructure getMultiBlockStucture();


    boolean wasActivated();

    void setActivated(boolean active);

    void updateFull();

    void updateFull(EntityPlayer player);


}
