package com.denfop.container;

import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolarPanels1 extends ContainerFullInv<TileSolarPanel> {

    public final TileSolarPanel tileentity;

    public ContainerSolarPanels1(EntityPlayer player, TileSolarPanel tileEntity1) {
        super(player, tileEntity1, 117 + 40 + 19 + 16 + 6 + 18, 229);
        this.tileentity = tileEntity1;
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slotDept, 0, 64, 51));


    }


}
