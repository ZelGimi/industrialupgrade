package com.denfop.container;

import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerWindGenerator extends ContainerFullInv<TileWindGenerator> {

    public ContainerWindGenerator(TileWindGenerator windGenerator, Player entityPlayer) {
        super(entityPlayer, windGenerator, 246);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 89, 19));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 30, 19));

    }


}
