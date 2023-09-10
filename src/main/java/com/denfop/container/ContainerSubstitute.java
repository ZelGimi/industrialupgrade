package com.denfop.container;

import com.denfop.tiles.mechanism.energy.TileEnergySubstitute;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSubstitute extends ContainerFullInv<TileEnergySubstitute> {

    public ContainerSubstitute(TileEnergySubstitute tileEntity1, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntity1, 179);
        for (int i = 0; i < 16; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.slot, i, 9 + (18 * (i % 4)), 17 + (18 * (i / 4))));

        }
    }


}
