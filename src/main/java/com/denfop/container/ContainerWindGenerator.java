package com.denfop.container;

import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerWindGenerator extends ContainerFullInv<TileWindGenerator> {

    public ContainerWindGenerator(TileWindGenerator windGenerator, Player entityPlayer) {
        super(entityPlayer, windGenerator, 208, 242);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 59, 135));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 28, 135));

    }


}
