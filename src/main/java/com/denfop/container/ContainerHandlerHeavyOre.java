package com.denfop.container;

import com.denfop.tiles.base.TileBaseHandlerHeavyOre;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHandlerHeavyOre extends ContainerFullInv<TileBaseHandlerHeavyOre> {

    public ContainerHandlerHeavyOre(EntityPlayer entityPlayer, TileBaseHandlerHeavyOre tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerHandlerHeavyOre(
            EntityPlayer entityPlayer,
            TileBaseHandlerHeavyOre tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    0, 37, 36
            ));
        }

        for (int i = 0; i < tileEntity1.outputSlot.size(); i++) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    i, 111 + (18 * (i / 3)), 18 + 18 * (i % 3)
            ));

        }

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 16, upgradeY + i * 18
            ));
        }
    }


}
