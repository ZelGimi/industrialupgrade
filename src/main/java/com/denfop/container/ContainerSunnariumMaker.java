package com.denfop.container;

import com.denfop.tiles.base.TileBaseSunnariumMaker;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSunnariumMaker extends ContainerFullInv<TileBaseSunnariumMaker> {

    public ContainerSunnariumMaker(EntityPlayer entityPlayer, TileBaseSunnariumMaker tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerSunnariumMaker(
            EntityPlayer entityPlayer,
            TileBaseSunnariumMaker tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA,
                    0, 38, 17
            ));
        }

        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA,
                    1, 74, 17
            ));
        }

        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA,
                    2, 38, 39
            ));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA,
                    3, 74, 39
            ));
        }

        if ((tileEntity1).outputSlot != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot,
                    0, 110 + 5, 34
            ));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).upgradeSlot,
                    i, upgradeX, upgradeY + i * 18
            ));
        }
    }


}
