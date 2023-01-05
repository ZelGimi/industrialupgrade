package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityOilPump;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerOilPump extends ContainerFullInv<TileEntityOilPump> {

    public ContainerOilPump(EntityPlayer entityPlayer, TileEntityOilPump tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("fluidTank");
        ret.add("count");
        ret.add("find");
        ret.add("maxcount");
        ret.add("level");
        ret.add("type");
        return ret;
    }

}
