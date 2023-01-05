package com.denfop.container;

import com.denfop.tiles.mechanism.solardestiller.TileEntityBaseSolarDestiller;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

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

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("skyLight");
        ret.add("inputTank");
        ret.add("outputTank");
        return ret;
    }

}
