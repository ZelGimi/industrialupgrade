package com.denfop.container;

import com.denfop.tiles.reactors.graphite.controller.TileEntityMainController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGraphiteReactor extends ContainerFullInv<TileEntityMainController> {

    public ContainerGraphiteReactor(TileEntityMainController tileEntityMainController, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntityMainController, 214, 250);
        addSlotToContainer(new SlotInvSlot(
                tileEntityMainController.scheduleReactor,
                0,
                71,
                141
        ));
        switch (this.base.getMaxLevelReactor()) {
            case 1:
                for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
                    for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                        addSlotToContainer(new SlotInvSlot(
                                tileEntityMainController.reactorsElements,
                                j * tileEntityMainController.getWidth() + i,
                                71 + i * 18,
                                29 + j * 18
                        ));
                    }
                }
                break;
            case 2:
                for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
                    for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                        addSlotToContainer(new SlotInvSlot(
                                tileEntityMainController.reactorsElements,
                                j * tileEntityMainController.getWidth() + i,
                                53 + i * 18,
                                20 + j * 18
                        ));
                    }
                }
                break;
            case 3:
                for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
                    for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                        addSlotToContainer(new SlotInvSlot(
                                tileEntityMainController.reactorsElements,
                                j * tileEntityMainController.getWidth() + i,
                                44 + i * 18,
                                11 + j * 18
                        ));
                    }
                }
                break;
            case 4:
                for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
                    for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                        addSlotToContainer(new SlotInvSlot(
                                tileEntityMainController.reactorsElements,
                                j * tileEntityMainController.getWidth() + i,
                                26 + i * 18,
                                12 + j * 18
                        ));
                    }
                }
                break;
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(
                    tileEntityMainController.reactorsModules,
                    i,
                    203,
                    112 + i * 19
            ));

        }
    }

}
