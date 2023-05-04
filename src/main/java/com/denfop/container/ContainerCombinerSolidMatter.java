package com.denfop.container;

import com.denfop.tiles.base.TileEntityCombinerSolidMatter;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerCombinerSolidMatter extends ContainerFullInv<TileEntityCombinerSolidMatter> {

    public ContainerCombinerSolidMatter(EntityPlayer entityPlayer, TileEntityCombinerSolidMatter tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, i, 12 + (i - (3 * count)) * 18, 16 + count * 18));

        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, i, 91 + (i - (3 * count)) * 18, 16 + count * 18));

        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        return ret;
    }

}
