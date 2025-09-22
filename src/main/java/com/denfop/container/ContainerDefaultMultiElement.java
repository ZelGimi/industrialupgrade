package com.denfop.container;

import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerDefaultMultiElement extends ContainerFullInv<TileEntityMultiBlockElement> {

    public ContainerDefaultMultiElement(TileEntityMultiBlockElement multiBlockElement, EntityPlayer var1) {
        super(multiBlockElement, var1);
        addSlots(multiBlockElement, var1);
    }

    public void addSlots(TileEntityMultiBlockElement multiBlockElement, EntityPlayer var1) {

    }

}
