package com.denfop.container;

import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWaterMainController extends ContainerFullInv<TileEntityMainController> {

    public ContainerWaterMainController(TileEntityMainController tileEntityMainController, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntityMainController, 188, 256);
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityMainController.reactorsModules,
                     i,
                    188,
                    40 + i * 18
            ));
        }
        for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
            for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                addSlotToContainer(new SlotInvSlot(tileEntityMainController.reactorsElements,
                        j * tileEntityMainController.getWidth() + i,
                        (30 + 10 * (3 - tileEntityMainController.enumFluidReactors.ordinal())) + i * 20,
                        (25+ 10 * (3 - tileEntityMainController.enumFluidReactors.ordinal())) + j * 20
                ));
            }
        }

    }

}
