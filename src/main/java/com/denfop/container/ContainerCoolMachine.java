package com.denfop.container;

import com.denfop.tiles.mechanism.cooling.TileEntityCooling;
import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerCoolMachine extends ContainerFullInv<TileEntityCooling> {

    public ContainerCoolMachine(EntityPlayer entityPlayer, TileEntityCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("cold");
        ret.add("max");
        ret.add("work");
        ret.add("sound");
        return ret;
    }

}
