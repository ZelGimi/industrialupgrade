package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntityAdvSteamQuarry;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuAdvSteamQuarry extends ContainerMenuFullInv<BlockEntityAdvSteamQuarry> {

    public ContainerMenuAdvSteamQuarry(Player entityPlayer, BlockEntityAdvSteamQuarry tileEntity1) {
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
