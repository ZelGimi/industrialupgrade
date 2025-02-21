package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityGenomeExtractor;
import com.denfop.tiles.mechanism.TileEntityTreeBreaker;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGenomeExtractor extends ContainerFullInv<TileEntityGenomeExtractor> {

    public ContainerGenomeExtractor(TileEntityGenomeExtractor tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.input, 0, 19 , 16));
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, 0, 19 , 34));

    }

}
