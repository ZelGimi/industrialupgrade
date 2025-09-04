package com.denfop.containermenu;

import com.denfop.blockentity.reactors.heat.controller.BlockEntityMainController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHeatReactor extends ContainerMenuFullInv<BlockEntityMainController> {

    public ContainerMenuHeatReactor(BlockEntityMainController tileEntityMainController, Player entityPlayer) {
        super(entityPlayer, tileEntityMainController, 214, 250);
        addSlotToContainer(new SlotInvSlot(
                tileEntityMainController.scheduleReactor,
                0,
                165,
                106
        ));
        switch (this.base.getMaxLevelReactor()) {
            case 1:
                for (int i = 0; i < tileEntityMainController.getWidth(); i++) {
                    for (int j = 0; j < tileEntityMainController.getHeight(); j++) {
                        addSlotToContainer(new SlotInvSlot(
                                tileEntityMainController.reactorsElements,
                                j * tileEntityMainController.getWidth() + i,
                                50 + i * 18,
                                37 + j * 18
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
                                41 + i * 18,
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
                                32 + i * 18,
                                20 + j * 18
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
                                23 + i * 18,
                                10 + j * 18
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
