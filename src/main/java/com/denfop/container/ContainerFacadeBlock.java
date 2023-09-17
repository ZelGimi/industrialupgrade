package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityFacadeBlock;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFacadeBlock extends ContainerFullInv<TileEntityFacadeBlock> {

    public ContainerFacadeBlock(final EntityPlayer player, final TileEntityFacadeBlock base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.stackSlot,
                0, 70, 40
        ));
    }

}
