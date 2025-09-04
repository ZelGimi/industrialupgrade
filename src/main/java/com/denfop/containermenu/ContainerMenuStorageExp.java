package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.exp.BlockEntityStorageExp;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuStorageExp extends ContainerMenuFullInv<BlockEntityStorageExp> {

    public ContainerMenuStorageExp(Player entityPlayer, BlockEntityStorageExp tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerMenuStorageExp(Player entityPlayer, BlockEntityStorageExp tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);

        addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot,
                0, 80, 41
        ));

    }


}
