package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileAdvSteamQuarry;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAdvSteamQuarry extends ContainerFullInv<TileAdvSteamQuarry> {

    public ContainerAdvSteamQuarry(EntityPlayer entityPlayer, TileAdvSteamQuarry tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        for (int i = 0; i < tileEntity1.output.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.output,
                    i, 28 + (i % 8) * 18, 20 + (i / 8) * 18
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.invSlot,
                0, 8, 20
        ));
        addSlotToContainer(new SlotInvSlot(tileEntity1.invSlot1,
                0, 8, 42
        ));
    }


}
