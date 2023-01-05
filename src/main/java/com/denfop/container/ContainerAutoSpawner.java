package com.denfop.container;

import com.denfop.tiles.base.TileEntityAutoSpawner;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerAutoSpawner extends ContainerFullInv<TileEntityAutoSpawner> {

    public ContainerAutoSpawner(EntityPlayer entityPlayer, TileEntityAutoSpawner tileEntity1) {
        this(entityPlayer, tileEntity1, 177);
        if (tileEntity1.outputSlot != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, j + i * 9, 8 + 18 * j, 18 + i * 18));
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.module_slot, i, 191, 18 + i * 18));
        }


        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.module_upgrade, i, 191, 99 + i * 18));
        }


    }

    public ContainerAutoSpawner(EntityPlayer entityPlayer, TileEntityAutoSpawner tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("progress");
        ret.add("energy2");
        ret.add("energy");
        ret.add("tempprogress");
        ret.add("exp");
        ret.add("description_mobs");
        return ret;
    }


}
