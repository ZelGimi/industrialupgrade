package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamHandlerHeavyOre;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamHandlerHeavyOre extends ContainerMenuFullInv<BlockEntitySteamHandlerHeavyOre> {

    public ContainerMenuSteamHandlerHeavyOre(Player entityPlayer, BlockEntitySteamHandlerHeavyOre tileEntity1) {
        this(entityPlayer, tileEntity1, 166);
    }

    public ContainerMenuSteamHandlerHeavyOre(
            Player entityPlayer,
            BlockEntitySteamHandlerHeavyOre tileEntity1,
            int height
    ) {
        super(entityPlayer, tileEntity1, height);
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    0, 37, 36
            ));
        }

        for (int i = 0; i < tileEntity1.outputSlot.size(); i++) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    i, 111 + (18 * (i / 3)), 18 + 18 * (i % 3)
            ));

        }


    }


}
