package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityCrystalCharger;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCrystalCharger extends ContainerMenuFullInv<BlockEntityCrystalCharger> {

    public ContainerMenuCrystalCharger(final Player player, final BlockEntityCrystalCharger base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
        addSlotToContainer(new SlotInvSlot(base.input_slot,
                0, -20, 84
        ));
    }


}
