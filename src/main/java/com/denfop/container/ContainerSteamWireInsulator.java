package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamWireInsulator;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamWireInsulator extends ContainerFullInv<TileSteamWireInsulator> {

    public ContainerSteamWireInsulator(Player var1, TileSteamWireInsulator tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 30, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1, 50, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 110, 44));

    }

}
