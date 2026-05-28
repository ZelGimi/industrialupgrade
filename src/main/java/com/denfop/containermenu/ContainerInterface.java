package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityInterface;
import net.minecraft.world.entity.player.Player;

public class ContainerInterface extends ContainerMenuFullInv<BlockEntityInterface> {

    public ContainerInterface(BlockEntityInterface tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 178, 203);

        for (int i = 0; i < tileEntity1.getSlots().size(); i++)
            addSlotToContainer(new SlotInvSlot(tileEntity1.getSlots(),
                    i, 8 + (i % 9) * 18, 16 + (i / 9) * 27
            ));

    }


}
