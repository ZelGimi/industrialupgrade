package com.denfop.container;


import com.denfop.tiles.mechanism.generator.things.fluid.TileEntityWaterGenerator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerWaterGenerator extends ContainerFullInv<TileEntityWaterGenerator> {

    public ContainerWaterGenerator(EntityPlayer entityPlayer, TileEntityWaterGenerator tileEntity1) {
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

        return ret;
    }

}
