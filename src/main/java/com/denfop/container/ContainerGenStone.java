package com.denfop.container;


import com.denfop.tiles.mechanism.TileEntityBaseGenStone;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerGenStone extends ContainerFullInv<TileEntityBaseGenStone> {

    public ContainerGenStone(EntityPlayer entityPlayer, TileEntityBaseGenStone tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 56 - 48, 53, 152, 8);
    }

    public ContainerGenStone(
            EntityPlayer entityPlayer, TileEntityBaseGenStone tileEntity1, int height, int dischargeX, int dischargeY,
            int upgradeX, int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1,
                    38, 10
            ));
        }

        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0,
                    38, 50
            ));
        }
        if (tileEntity1.outputSlot != null) {
            for (int i = 0; i < tileEntity1.outputSlot.size(); i++) {
                int count = i / 3;
                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, i, 90 + (i - (3 * count)) * 18, 8 + count * 18));


            }
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, upgradeX, upgradeY + i * 18
            ));
        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("energy");
        ret.add("mode");

        return ret;
    }

}
