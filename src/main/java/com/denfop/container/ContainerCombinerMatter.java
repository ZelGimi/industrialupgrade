package com.denfop.container;

import com.denfop.Config;
import com.denfop.tiles.base.TileEntityCombinerMatter;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerCombinerMatter extends ContainerFullInv<TileEntityCombinerMatter> {

    public ContainerCombinerMatter(EntityPlayer entityPlayer, TileEntityCombinerMatter tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        if (Config.amplifierSlot) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.amplifierSlot, 0, 72, 40));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, i, 12 + (i - (3 * count)) * 18, 16 + count * 18));

        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("scrap");
        ret.add("fluidTank");
        return ret;
    }

}
