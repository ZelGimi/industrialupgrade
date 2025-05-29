package com.denfop.container;

import com.denfop.tiles.mechanism.exp.TileStorageExp;
import net.minecraft.world.entity.player.Player;

public class ContainerStorageExp extends ContainerFullInv<TileStorageExp> {

    public ContainerStorageExp(Player entityPlayer, TileStorageExp tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerStorageExp(Player entityPlayer, TileStorageExp tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);

        addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot,
                0, 80, 41
        ));

    }


}
