package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.generator.energy.BlockEntityPeatGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPeatGenerator extends ContainerMenuFullInv<BlockEntityPeatGenerator> {

    public ContainerMenuPeatGenerator(Player entityPlayer, BlockEntityPeatGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slot, 0, 65, 53));
    }


}
