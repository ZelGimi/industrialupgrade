package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityBaseRedstoneGenerator;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerRedstoneGenerator extends ContainerFullInv<TileEntityBaseRedstoneGenerator> {

    public ContainerRedstoneGenerator(EntityPlayer entityPlayer, TileEntityBaseRedstoneGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slot, 0, 65, 53));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("fuel");
        return ret;
    }

}
