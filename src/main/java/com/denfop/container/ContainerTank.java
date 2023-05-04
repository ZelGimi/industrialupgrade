package com.denfop.container;

import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerTank extends ContainerFullInv<TileEntityLiquedTank> {

    public ContainerTank(EntityPlayer entityPlayer, TileEntityLiquedTank tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot1, 0, 60, 36));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("fluidTank");
        return ret;
    }

}
