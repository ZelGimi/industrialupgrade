package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerResearchTableSpace extends ContainerFullInv<TileEntityResearchTableSpace> {

    public final EntityPlayer player;

    public ContainerResearchTableSpace(TileEntityResearchTableSpace tileEntityResearchTableSpace, EntityPlayer var1) {
        super(var1,tileEntityResearchTableSpace,250);
        this.addSlotToContainer(new SlotInvSlot(tileEntityResearchTableSpace.slotLens, 0, 197, 230));
        this.player = var1;
    }
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.base.timer = 5;
    }
}
