package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityFieldCleaner;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuFieldCleaner extends ContainerMenuFullInv<BlockEntityFieldCleaner> {

    public ContainerMenuFieldCleaner(BlockEntityFieldCleaner tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 150, 10 + i * 18));
        }
    }

}
