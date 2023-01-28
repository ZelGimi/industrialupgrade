package com.simplequarries;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerBaseQuarry extends ContainerFullInv<TileEntityBaseQuarry> {

    public ContainerBaseQuarry(EntityPlayer entityPlayer, TileEntityBaseQuarry tileEntity1) {
        this(entityPlayer, tileEntity1, 166);
        for (int j = 0; j < tileEntity1.input.size(); ++j) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.input,
                    j, 6, 6 + j * 18
            ));
        }
        if (tileEntity1.outputSlot != null) {

            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j, 30 + 18 * j, 6
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 6, 30 + 18 * j, 6 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 12, 30 + 18 * j, 6 + 18 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 18, 30 + 18 * j, 6 + 18 + 18 + 18
                ));
            }

        }
    }

    public ContainerBaseQuarry(EntityPlayer entityPlayer, TileEntityBaseQuarry tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energyconsume");
        ret.add("energy");
        ret.add("blockpos");
        ret.add("energy1");
        ret.add("cold");
        ret.add("col");
        ret.add("min_y");
        ret.add("max_y");
        ret.add("exp");
        ret.add("vein_need");
        ret.add("need_work");
        ret.add("default_pos");
        ret.add("chunkx1");
        ret.add("chunkz1");
        ret.add("chunkx2");
        ret.add("chunkz2");
        return ret;
    }


}
