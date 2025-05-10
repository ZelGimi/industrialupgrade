package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityFluidMixer;
import net.minecraft.world.entity.player.Player;

public class ContainerFluidMixer extends ContainerFullInv<TileEntityFluidMixer> {

    public ContainerFluidMixer(Player entityPlayer, TileEntityFluidMixer tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 25, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 48, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 2, 99, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 3, 122, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 25, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 48, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot3, 0, 99, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot4, 0, 122, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
