package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamHandlerHeavyOre;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamHandlerHeavyOre extends ContainerFullInv<TileSteamHandlerHeavyOre> {

    public ContainerSteamHandlerHeavyOre(EntityPlayer entityPlayer, TileSteamHandlerHeavyOre tileEntity1) {
        this(entityPlayer, tileEntity1, 166);
    }

    public ContainerSteamHandlerHeavyOre(
            EntityPlayer entityPlayer,
            TileSteamHandlerHeavyOre tileEntity1,
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
