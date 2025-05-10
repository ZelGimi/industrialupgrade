package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.TilePeatGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerPeatGenerator extends ContainerFullInv<TilePeatGenerator> {

    public ContainerPeatGenerator(Player entityPlayer, TilePeatGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slot, 0, 65, 53));
    }


}
