package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityAlkalineEarthQuarry;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuAlkalineEarthQuarry extends ContainerMenuFullInv<BlockEntityAlkalineEarthQuarry> {

    public ContainerMenuAlkalineEarthQuarry(BlockEntityAlkalineEarthQuarry tleEntityMatterFactory, Player var1) {
        super(tleEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 0, 30, 45));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotB, 0, 60, 64));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.outputSlot, 0, 90, 45));
        for (int i = 0; i < 2; i++) {
            addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.upgradeSlot,
                    i, 128, 15 + i * 18
            ));
        }
    }

}
