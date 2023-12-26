package com.denfop.container;

import com.denfop.tiles.reactors.graphite.controller.TileEntityMainController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGraphiteReactor extends ContainerFullInv<TileEntityMainController>{

    public ContainerGraphiteReactor(TileEntityMainController tileEntityMainController, EntityPlayer entityPlayer) {
        super(entityPlayer,tileEntityMainController,188,250);
        for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
            for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                addSlotToContainer(new SlotInvSlot(tileEntityMainController.reactorsElements,
                        j * tileEntityMainController.getWidth() + i,
                        (67 - 10 * ( tileEntityMainController.enumFluidReactors.ordinal())) + i * 18,
                        (11 ) + j * 18
                ));
            }
        }
        for (int i = 0; i < 4; i++) {
                addSlotToContainer(new SlotInvSlot(tileEntityMainController.reactorsModules,
                        i,
                        190 + i * 18,
                        170
                ));

        }
    }

}
