package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityVolcanoChest;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerVolcanoChest extends ContainerFullInv<TileEntityVolcanoChest> {

    public ContainerVolcanoChest(TileEntityVolcanoChest tileEntityVolcanoChest, EntityPlayer var1) {
        super(tileEntityVolcanoChest, var1);
        for (int i = 0; i < 27; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityVolcanoChest.invSlot, i, 8 + (i % 9) * 18, 22 + (i / 9) * 18));
        }
    }

}
