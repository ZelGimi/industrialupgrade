package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityFacadeBlock;
import net.minecraft.world.entity.player.Player;

public class ContainerFacadeBlock extends ContainerFullInv<TileEntityFacadeBlock> {

    public ContainerFacadeBlock(final Player player, final TileEntityFacadeBlock base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.stackSlot,
                0, 70, 40
        ));
    }

}
