package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityElectricDryer;
import net.minecraft.world.entity.player.Player;

public class ContainerDryer extends ContainerFullInv<TileEntityElectricDryer> {

    public ContainerDryer(Player entityPlayer, TileEntityElectricDryer tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 0, 45, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 100, 40));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 45, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
