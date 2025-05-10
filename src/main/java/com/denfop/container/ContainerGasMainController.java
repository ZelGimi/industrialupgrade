package com.denfop.container;

import com.denfop.tiles.reactors.gas.controller.TileEntityMainController;
import net.minecraft.world.entity.player.Player;

public class ContainerGasMainController extends ContainerFullInv<TileEntityMainController> {

    public ContainerGasMainController(TileEntityMainController tileEntityMainController, Player entityPlayer) {
        super(entityPlayer, tileEntityMainController, 214, 250);
        addSlotToContainer(new SlotInvSlot(
                tileEntityMainController.scheduleReactor,
                0,
                167,
                104
        ));
        switch (this.base.getMaxLevelReactor()) {
            case 1:
                for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
                    for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                        addSlotToContainer(new SlotInvSlot(
                                tileEntityMainController.reactorsElements,
                                j * tileEntityMainController.getWidth() + i,
                                57 + i * 18,
                                36 + j * 18
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
                                48 + i * 18,
                                28 + j * 18
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
                                39 + i * 18,
                                18 + j * 18
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
                                21 + i * 18,
                                18 + j * 18
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
