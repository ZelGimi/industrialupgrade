package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntitySolidFluidIntegrator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSolidFluidIntegrator extends ContainerMenuFullInv<BlockEntitySolidFluidIntegrator> {

    public ContainerMenuSolidFluidIntegrator(Player var1, BlockEntitySolidFluidIntegrator tileEntity1) {
        super(var1, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 115, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 0, 36, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 1, 60, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 2, 143, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 36, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 143, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot3, 0, 60, 79));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 172, 21 + i * 18
            ));
        }
    }

}
