package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntitySafe;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSafe extends ContainerMenuFullInv<BlockEntitySafe> {

    public ContainerMenuSafe(BlockEntitySafe tileEntitySafe, Player var1) {
        super(var1, tileEntitySafe, 233);
        for (int i = 0; i < tileEntitySafe.slot.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntitySafe.slot, i, 10 + (i % 9) * 18, 18 + (i / 9) * 18));
        }
    }


    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.base.setActive(true);
        this.base.timer = 5;
    }


}
