package com.denfop.container;

import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.world.entity.player.Player;

public class ContainerSolarPanels extends ContainerFullInv<TileSolarPanel> {

    public final TileSolarPanel tileentity;

    public ContainerSolarPanels(Player player, TileSolarPanel tileEntity1) {
        super(player, tileEntity1, 117 + 40 + 19 + 16 + 6 + 18, 229);
        this.tileentity = tileEntity1;
        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot, j, 27 + j * 18, 121));

        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slotDept, 0, 64, 51){
            @Override
            public boolean isActive() {
                return false;
            }
        });


    }


}
