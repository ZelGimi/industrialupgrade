package com.denfop.container;

import com.denfop.tiles.mechanism.energy.TileEnergySubstitute;
import net.minecraft.world.entity.player.Player;

public class ContainerSubstitute extends ContainerFullInv<TileEnergySubstitute> {

    public ContainerSubstitute(TileEnergySubstitute tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1,178);
        for (int i = 0; i < 16; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.slot, i, 40 + (18 * (i % 4)), 18 + (18 * (i / 4))));

        }
    }


}
