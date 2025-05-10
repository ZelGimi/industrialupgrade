package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityNuclearWasteRecycler;
import net.minecraft.world.entity.player.Player;

public class ContainerNuclearWasteRecycler extends ContainerFullInv<TileEntityNuclearWasteRecycler> {

    public ContainerNuclearWasteRecycler(TileEntityNuclearWasteRecycler tileEntityNuclearWasteRecycler, Player var1) {
        super(tileEntityNuclearWasteRecycler, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityNuclearWasteRecycler.inputSlotA, 0, 70, 17));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
