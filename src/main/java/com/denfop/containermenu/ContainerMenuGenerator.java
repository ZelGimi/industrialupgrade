package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.generator.energy.coal.BlockEntityAdvGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGenerator extends ContainerMenuFullInv<BlockEntityAdvGenerator> {

    public ContainerMenuGenerator(Player entityPlayer, BlockEntityAdvGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fuelSlot, 0, 65, 53));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.chargeSlot, 0, 65, 17));
    }


}
