package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityShield;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuShield extends ContainerMenuFullInv<BlockEntityShield> {

    public ContainerMenuShield(BlockEntityShield tileEntityShield, Player var1) {
        super(tileEntityShield, var1);
        for (int i = 0; i < tileEntityShield.getSlot().size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityShield.getSlot(),
                    i, 8 + (i % 9) * 18, 20 + (i / 9) * 18
            ));
        }
    }

}
