package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityGenomeExtractor;
import net.minecraft.world.entity.player.Player;

public class ContainerGenomeExtractor extends ContainerFullInv<TileEntityGenomeExtractor> {

    public ContainerGenomeExtractor(TileEntityGenomeExtractor tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.input, 0, 19, 16));
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, 0, 19, 34));

    }

}
