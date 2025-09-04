package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.generator.energy.redstone.BlockEntityBaseRedstoneGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRedstoneGenerator extends ContainerMenuFullInv<BlockEntityBaseRedstoneGenerator> {

    public ContainerMenuRedstoneGenerator(Player entityPlayer, BlockEntityBaseRedstoneGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slot, 0, 65, 53));
    }


}
