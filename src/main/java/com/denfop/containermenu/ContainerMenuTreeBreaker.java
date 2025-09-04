package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityTreeBreaker;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuTreeBreaker extends ContainerMenuFullInv<BlockEntityTreeBreaker> {

    public ContainerMenuTreeBreaker(BlockEntityTreeBreaker tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 18; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, i, 10 + (i % 9) * 18, 18 + 18 * (i / 9)));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
