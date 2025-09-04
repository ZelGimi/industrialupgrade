package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityMoonSpotter;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuMoonSpotter extends ContainerMenuFullInv<BlockEntityMoonSpotter> {

    public ContainerMenuMoonSpotter(BlockEntityMoonSpotter tleEntityMatterFactory, Player var1) {
        super(tleEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 0, 70, 17));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.outputSlot, 0, 70, 60));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.upgradeSlot,
                    i, 152, 8 + i * 18
            ));
        }
    }

}
