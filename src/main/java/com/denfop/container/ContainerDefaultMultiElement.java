package com.denfop.container;

import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.world.entity.player.Player;

public class ContainerDefaultMultiElement extends ContainerFullInv<TileEntityMultiBlockElement> {

    public ContainerDefaultMultiElement(TileEntityMultiBlockElement multiBlockElement, Player var1) {
        super(multiBlockElement, var1);
        addSlots(multiBlockElement, var1);
    }

    public void addSlots(TileEntityMultiBlockElement multiBlockElement, Player var1) {

    }

}
