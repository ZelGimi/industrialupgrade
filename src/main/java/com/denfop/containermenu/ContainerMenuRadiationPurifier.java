package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityRadiationPurifier;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRadiationPurifier extends ContainerMenuFullInv<BlockEntityRadiationPurifier> {

    public ContainerMenuRadiationPurifier(BlockEntityRadiationPurifier tileRadiationPurifier, Player entityPlayer) {
        super(tileRadiationPurifier, entityPlayer);
        this.addSlotToContainer(new SlotInvSlot(tileRadiationPurifier.outputSlot, 0, 79, 37));
    }

}
