package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityShield;
import net.minecraft.world.entity.player.Player;

public class ContainerShield extends ContainerFullInv<TileEntityShield> {

    public ContainerShield(TileEntityShield tileEntityShield, Player var1) {
        super(tileEntityShield, var1);
        for (int i = 0; i < tileEntityShield.getSlot().size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityShield.getSlot(),
                    i, 8 + (i % 9) * 18, 20 + (i / 9) * 18
            ));
        }
    }

}
