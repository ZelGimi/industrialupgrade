package com.denfop.container;


import com.denfop.tiles.mechanism.TileBaseReplicator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerReplicator extends ContainerElectricMachine<TileBaseReplicator> {

    public ContainerReplicator(EntityPlayer player, TileBaseReplicator tileEntity1) {
        super(player, tileEntity1, 184, 152, 83);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 91, 59));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 8, 27));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.cellSlot, 0, 8, 72));

        for (int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }

    }


}
