package com.denfop.container;


import com.denfop.tiles.base.TileScanner;
import net.minecraft.world.entity.player.Player;

public class ContainerScanner extends ContainerElectricMachine<TileScanner> {

    public ContainerScanner(Player player, TileScanner tileEntity1) {
        super(player, tileEntity1, 166, 8, 43);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, 0, 55, 35));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.diskSlot, 0, 152, 65));
    }


}
