package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerGeoGenerator extends ContainerFullInv<TileEntityGeoGenerator> {

    public ContainerGeoGenerator(EntityPlayer entityPlayer, TileEntityGeoGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.chargeSlot, 0, 117, 49));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("fluidTank");
        return ret;
    }

}
