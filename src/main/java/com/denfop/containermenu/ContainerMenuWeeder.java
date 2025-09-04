package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityWeeder;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWeeder extends ContainerMenuFullInv<BlockEntityWeeder> {

    public ContainerMenuWeeder(BlockEntityWeeder tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, 0, 80, 18));
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
