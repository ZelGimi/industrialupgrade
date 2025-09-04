package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.energy.BlockEntityEnergyRemover;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRemover extends ContainerMenuFullInv<BlockEntityEnergyRemover> {

    public ContainerMenuRemover(BlockEntityEnergyRemover tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 178);
        for (int i = 0; i < 16; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.slot, i, 40 + (18 * (i % 4)), 18 + (18 * (i / 4))));

        }
    }


}
