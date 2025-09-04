package com.denfop.containermenu;

import com.denfop.blockentity.reactors.graphite.exchanger.BlockEntityExchanger;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuExchanger extends ContainerMenuFullInv<BlockEntityExchanger> {

    public ContainerMenuExchanger(BlockEntityExchanger tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 208);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
