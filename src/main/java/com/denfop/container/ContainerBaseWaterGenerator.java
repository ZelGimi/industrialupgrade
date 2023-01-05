package com.denfop.container;

import com.denfop.tiles.mechanism.water.TileEntityBaseWaterGenerator;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerBaseWaterGenerator extends ContainerFullInv<TileEntityBaseWaterGenerator> {

    public ContainerBaseWaterGenerator(TileEntityBaseWaterGenerator windGenerator, EntityPlayer entityPlayer) {
        super(entityPlayer, windGenerator, 236);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 89, 19));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 30, 19));

    }

    @Override
    public List<String> getNetworkedFields() {
        final List<String> ret = super.getNetworkedFields();
        ret.add("coefficient");
        ret.add("speed");
        ret.add("slot");
        ret.add("rotorSide");
        ret.add("generation");
        ret.add("energy");
        ret.add("timers");
        ret.add("wind_speed");
        ret.add("wind_side");
        ret.add("mind_wind");
        ret.add("mind_speed");
        ret.add("enumTypeWind");
        return ret;
    }

}
