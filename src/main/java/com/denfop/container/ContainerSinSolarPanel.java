package com.denfop.container;

import com.denfop.tiles.base.TileEntitySintezator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerSinSolarPanel extends ContainerFullInv<TileEntitySintezator> {

    public final TileEntitySintezator tileentity;

    public ContainerSinSolarPanel(EntityPlayer entityPlayer, TileEntitySintezator tileEntity1) {
        super(entityPlayer, tileEntity1, 117 + 40 + 19 + 16 + 4, 186 - 18);
        this.tileentity = tileEntity1;

        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(this.tileentity.inputslot, j, 17 + j * 18, 59));
        }
        for (int j = 0; j < 4; ++j) {

            this.addSlotToContainer(new SlotInvSlot(this.tileentity.inputslotA, j, 107 + j * 18, 28));
        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("sunIsUp");
        ret.add("skyIsVisible");
        ret.add("generating");
        ret.add("genDay");
        ret.add("genNight");
        ret.add("storage");
        ret.add("maxStorage");
        ret.add("production");
        ret.add("rain");
        ret.add("machineTire");
        ret.add("machineTire1");
        ret.add("progress");
        ret.add("inputslot");
        ret.add("type");
        ret.add("solartype");
        return ret;
    }

}
