package com.denfop.container;

import com.denfop.tiles.base.TileEntityBaseGenerationMicrochip;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerBaseGenerationChipMachine
        extends ContainerFullInv<TileEntityBaseGenerationMicrochip> {

    public ContainerBaseGenerationChipMachine(EntityPlayer entityPlayer, TileEntityBaseGenerationMicrochip tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerBaseGenerationChipMachine(
            EntityPlayer entityPlayer,
            TileEntityBaseGenerationMicrochip tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 10, 9));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 1, 10, 30));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 2, 43, 9));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 3, 43, 29));
        }
        if ((tileEntity1).outputSlot != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot, 0, 112, 19));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 4, 71, 19));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).upgradeSlot, i, upgradeX, upgradeY + i * 18));
        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("guiChargeLevel");
        ret.add("tier");
        ret.add("heat");
        ret.add("energy");
        return ret;
    }

}
