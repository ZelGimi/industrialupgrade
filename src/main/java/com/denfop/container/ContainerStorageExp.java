package com.denfop.container;

import com.denfop.tiles.base.TileEntityStorageExp;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerStorageExp extends ContainerFullInv<TileEntityStorageExp> {

    public ContainerStorageExp(EntityPlayer entityPlayer, TileEntityStorageExp tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerStorageExp(EntityPlayer entityPlayer, TileEntityStorageExp tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);

        addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot,
                0, 80, 41
        ));

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();

        ret.add("storage");
        ret.add("storage1");
        ret.add("expirencelevel");

        ret.add("expirencelevel1");
        return ret;
    }

}
