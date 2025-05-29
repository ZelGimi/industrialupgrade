package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamCrystalCharge;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamCharger extends ContainerFullInv<TileSteamCrystalCharge> {

    public ContainerSteamCharger(Player var1, TileSteamCrystalCharge tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 60, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 110, 44));

    }

}
