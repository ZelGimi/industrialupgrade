package com.denfop.container;

import com.denfop.tiles.base.TileEntityFisher;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerFisher extends ContainerFullInv<TileEntityFisher> {

    public ContainerFisher(EntityPlayer entityPlayer, TileEntityFisher tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

        addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot, 0, 17,
                45
        ));

        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, i, 65 + (i - (3 * count)) * 18, 27 + count * 18));

        }
    }

    public ContainerFisher(EntityPlayer entityPlayer, TileEntityFisher tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("sound");
        ret.add("energy");
        ret.add("progress");
        return ret;
    }

}
