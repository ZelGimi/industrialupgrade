package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamQuarry;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamQuarry extends ContainerFullInv<TileSteamQuarry> {

    public ContainerSteamQuarry(EntityPlayer entityPlayer, TileSteamQuarry tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        for (int i = 0; i < tileEntity1.output.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.output,
                    i, 28 + (i % 8) * 18, 20 + (i / 8) * 18
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.inventory,
                0, 8, 20
        ));
        addSlotToContainer(new SlotInvSlot(tileEntity1.inventory1,
                0, 8, 42
        ));
    }


}
