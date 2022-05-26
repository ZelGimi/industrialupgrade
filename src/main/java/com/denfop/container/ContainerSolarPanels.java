package com.denfop.container;

import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerSolarPanels extends ContainerFullInv<TileEntitySolarPanel> {

    public final TileEntitySolarPanel tileentity;

    public ContainerSolarPanels(EntityPlayer player, TileEntitySolarPanel tileEntity1) {
        super(player, tileEntity1, 117 + 40 + 19 + 16 + 4, 186 - 18);
        this.tileentity = tileEntity1;
        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot, j, 17 + j * 18, 59));

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
        ret.add("storage2");
        ret.add("maxStorage2");
        ret.add("production");
        ret.add("rain");
        ret.add("solarType");
        ret.add("rf");
        ret.add("getmodulerf");
        ret.add("type");
        ret.add("u");
        ret.add("p");
        ret.add("k");
        ret.add("m");
        ret.add("time");
        ret.add("time1");
        ret.add("time2");
        ret.add("tier");
        ret.add("solarpanels");
        ret.add("wireless");
        return ret;
    }


}
