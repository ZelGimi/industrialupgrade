package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSafe extends ContainerFullInv<TileEntitySafe> {

    public ContainerSafe(TileEntitySafe tileEntitySafe, EntityPlayer var1) {
        super(var1, tileEntitySafe, 233);
        for (int i = 0; i < tileEntitySafe.slot.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntitySafe.slot, i, 10 + (i % 9) * 18, 18 + (i / 9) * 18));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.base.setActive(true);
        this.base.timer = 5;
    }


}
