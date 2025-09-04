package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityFacadeBlock;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuFacadeBlock extends ContainerMenuFullInv<BlockEntityFacadeBlock> {

    public ContainerMenuFacadeBlock(final Player player, final BlockEntityFacadeBlock base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.stackSlot,
                0, 70, 40
        ));
    }

}
