package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuDefaultMultiElement extends ContainerMenuFullInv<BlockEntityMultiBlockElement> {

    public ContainerMenuDefaultMultiElement(BlockEntityMultiBlockElement multiBlockElement, Player var1) {
        super(multiBlockElement, var1);
        addSlots(multiBlockElement, var1);
    }

    public void addSlots(BlockEntityMultiBlockElement multiBlockElement, Player var1) {

    }

}
