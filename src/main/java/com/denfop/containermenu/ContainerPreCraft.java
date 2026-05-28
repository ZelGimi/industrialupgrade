package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityPreCraft;
import com.denfop.utils.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ContainerPreCraft extends ContainerMenuFullInv<BlockEntityPreCraft> {


    public ContainerPreCraft(BlockEntityPreCraft tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 238, 192, true);

        for (int i = 0; i < 10; i++) {

            int index = i % 10;

            int x = 8 + (index / 5) * 114;
            int y = 12 + (index % 5) * 18;

            addSlotToContainer(new SlotVirtualPreCraft(tileEntity1, i, x, y, tileEntity1.inputItems));
        }

    }


    public ItemStack quickMoveStack(Player player, int sourceSlotIndex) {


        return ModUtils.emptyStack;

    }
}
