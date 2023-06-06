package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.TileEntitySolarGenerator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerSolar extends ContainerFullInv<TileEntitySolarGenerator> {

    public ContainerSolar(TileEntitySolarGenerator tileEntity1, EntityPlayer player) {
        super(player, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.chargeSlot, 0, 80, 26));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("sunIsUp");
        ret.add("skyIsVisible");
        ret.add("rain");
        return ret;
    }

}
