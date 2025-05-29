package com.denfop.container;

import com.denfop.tiles.mechanism.TilePump;
import net.minecraft.world.entity.player.Player;

public class ContainerPump extends ContainerElectricMachine<TilePump> {

    public ContainerPump(Player entityPlayer, TilePump tileEntity1) {
        super(entityPlayer, tileEntity1, 166, 8, 44);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerSlot, 0, 99, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 132, 34));

        for (int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }

    }


}
