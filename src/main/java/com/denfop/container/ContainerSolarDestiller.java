package com.denfop.container;

import com.denfop.tiles.mechanism.solardestiller.TileEntityBaseSolarDestiller;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolarDestiller extends ContainerFullInv<TileEntityBaseSolarDestiller> {

    public ContainerSolarDestiller(EntityPlayer player, TileEntityBaseSolarDestiller tileEntite) {
        super(player, tileEntite, 184);
        this.addSlotToContainer(new SlotInvSlot(tileEntite.waterinputSlot, 0, 17, 27));
        this.addSlotToContainer(new SlotInvSlot(tileEntite.destiwaterinputSlot, 0, 136, 64));
        this.addSlotToContainer(new SlotInvSlot(tileEntite.wateroutputSlot, 0, 17, 45));
        this.addSlotToContainer(new SlotInvSlot(tileEntite.destiwateroutputSlott, 0, 136, 82));

        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntite.upgradeSlot, i, 152, 8 + i * 18));
        }

    }


}
