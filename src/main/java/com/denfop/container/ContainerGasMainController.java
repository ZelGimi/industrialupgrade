package com.denfop.container;

import com.denfop.tiles.reactors.gas.controller.TileEntityMainController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasMainController extends ContainerFullInv<TileEntityMainController>{

    public ContainerGasMainController(TileEntityMainController tileEntityMainController, EntityPlayer entityPlayer) {
   super(entityPlayer,tileEntityMainController,189,254);
        for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
            for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                addSlotToContainer(new SlotInvSlot(tileEntityMainController.reactorsElements,
                        j * tileEntityMainController.getWidth() + i,
                        (68 - 10 * ( tileEntityMainController.enumFluidReactors.ordinal())) + i * 20,
                        (16+ (3 - tileEntityMainController.enumFluidReactors.ordinal())) + j * 20
                ));
            }
        }
        for (int i = 0; i < 4; i++) {

                addSlotToContainer(new SlotInvSlot(tileEntityMainController.reactorsModules,
                        i,
                        187 + i * 19,
                        230
                ));

        }
    }

}
