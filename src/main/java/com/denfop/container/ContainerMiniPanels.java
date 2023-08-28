package com.denfop.container;

import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMiniPanels extends ContainerFullInv<TileEntityMiniPanels> {

    private final TileEntityMiniPanels tileentity;

    public ContainerMiniPanels(TileEntityMiniPanels tileEntityMiniPanels, EntityPlayer var1) {
        super(var1, tileEntityMiniPanels, 196, 206);
        this.tileentity = tileEntityMiniPanels;
        for (int j = 0; j < 4; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntityMiniPanels.invSlotStorage, j, 121, 21 + j * 18));

        }
        for (int j = 0; j < 4; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntityMiniPanels.invSlotOutput, j, 27, 21 + j * 18));

        }
        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntityMiniPanels.invSlotGlass, j, 48 + (j % 3) * 26,
                    22 + 26 * (j / 3)
            ));

        }
    }

    public TileEntityMiniPanels getTileEntity() {
        return tileentity;
    }


}
