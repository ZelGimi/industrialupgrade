package com.denfop.container;

import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolidMatter extends ContainerFullInv<TileEntityMatterGenerator> {

    public ContainerSolidMatter(EntityPlayer entityPlayer, TileEntityMatterGenerator tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerSolidMatter(
            EntityPlayer entityPlayer,
            TileEntityMatterGenerator tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if (tileEntity1.outputSlot != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    0, 69, 32
            ));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, upgradeX, upgradeY + i * 18
            ));
        }
    }


}
