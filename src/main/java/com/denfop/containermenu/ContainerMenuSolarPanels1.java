package com.denfop.containermenu;

import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSolarPanels1 extends ContainerMenuFullInv<BlockEntitySolarPanel> {

    public final BlockEntitySolarPanel tileentity;

    public ContainerMenuSolarPanels1(Player player, BlockEntitySolarPanel tileEntity1) {
        super(player, tileEntity1, 117 + 40 + 19 + 16 + 6 + 18, 229);
        this.tileentity = tileEntity1;
        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot, j, 27 + j * 18, 121));

        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slotDept, 0, 64, 51));


    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

    }
}
