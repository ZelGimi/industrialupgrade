package com.denfop.containermenu;


import com.denfop.blockentity.mechanism.generator.things.fluid.BlockEntityHeliumGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHeliumGenerator extends ContainerMenuFullInv<BlockEntityHeliumGenerator> {

    public ContainerMenuHeliumGenerator(Player entityPlayer, BlockEntityHeliumGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }


}
