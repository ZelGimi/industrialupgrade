package com.denfop.containermenu;

import com.denfop.blockentity.panels.entity.BlockEntityMiniPanels;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuMiniPanels extends ContainerMenuFullInv<BlockEntityMiniPanels> {

    private final BlockEntityMiniPanels tileentity;

    public ContainerMenuMiniPanels(BlockEntityMiniPanels tileEntityMiniPanels, Player var1) {
        super(var1, tileEntityMiniPanels, 204, 234);
        this.tileentity = tileEntityMiniPanels;
        for (int j = 0; j < 4; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntityMiniPanels.invSlotStorage, j, 143, 24 + j * 22));

        }
        for (int j = 0; j < 2; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntityMiniPanels.invSlotCore, j, 43 + j * 100, 118));

        }
        for (int j = 0; j < 4; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntityMiniPanels.invSlotOutput, j, 43, 24 + j * 22));

        }
        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntityMiniPanels.invSlotGlass, j, 70 + (j % 3) * 23,
                    34 + 23 * (j / 3)
            ));

        }
    }

    public BlockEntityMiniPanels getTileEntity() {
        return tileentity;
    }


}
